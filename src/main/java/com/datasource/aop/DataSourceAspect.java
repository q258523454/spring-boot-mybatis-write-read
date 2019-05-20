package com.datasource.aop;

import com.datasource.annotation.DataSourceAnnotation;
import com.datasource.util.DataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.activation.DataContentHandler;
import java.lang.reflect.Method;

/**
 * AOP根据注解给上下文赋值
 */
@Aspect
@Order(1)    // 数据源的切换要在数据库事务之前, 设置AOP执行顺序(需要在事务之前，否则事务只发生在默认库中, 数值越小等级越高)
@Component
public class DataSourceAspect {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    // 切点, 注意这里是在service层
    @Pointcut("execution(* com.service..*.*(..)))")
    public void aspect() {
    }

    @Before("aspect()")
    private void before(JoinPoint point) {
        Object target = point.getTarget();
        String method = point.getSignature().getName();
        Class<?> classz = target.getClass();
        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
        try {
            Method m = classz.getMethod(method, parameterTypes);
            if (m != null && m.isAnnotationPresent(DataSourceAnnotation.class)) {
                DataSourceAnnotation data = m.getAnnotation(DataSourceAnnotation.class);
                DataSourceContextHolder.putDataSource(data.value().getName());
                log.info("-----------切换数据源, 上下文准备赋值-----:{}", data.value().getName());
                log.info("-----------切换数据源, 数据源上下文实际赋值-----:{}", DataSourceContextHolder.getCurrentDataSource());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 切面结束, 重置线程变量
    @After("aspect()")
    public void after(JoinPoint joinPoint) {
        DataSourceContextHolder.removeCurrentDataSource();
        log.info("重置数据源: Restore DataSource to [{}] in Method [{}]", DataSourceContextHolder.getCurrentDataSource(), joinPoint.getSignature());
    }
}
