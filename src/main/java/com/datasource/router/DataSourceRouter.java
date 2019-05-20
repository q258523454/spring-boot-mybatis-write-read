package com.datasource.router;

import com.datasource.util.DataSourceContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

/***
 * AbstractRoutingDataSource抽象类, 实现AOP动态切换的关键
 * 		1.AbstractRoutingDataSource中determineTargetDataSource()方法中获取数据源 
 * 			Object lookupKey = determineCurrentLookupKey();
 * 			DataSource dataSource = this.resolvedDataSources.get(lookupKey);
 * 			根据determineCurrentLookupKey()得到Datasource,并且此方法是抽象方法,应用可以实现
 *     2.resolvedDataSources 的值根据 targetDataSources 所得 afterPropertiesSet()方法[该方法在@Bean所在方法执行完成后执行]中:
 * 			Map.Entry<Object, Object> entry : this.targetDataSources.entrySet()
 *     3.然后在xml中使用<bean>或者代码中@Bean 设置 dataSource 的defaultTargetDataSource(默认数据源)和 targetDataSources(多数据源)
 *     4.利用自定义注解，AOP拦截动态的设置ThreadLocal的值
 *     5.在DAO层与数据库建立连接时会根据ThreadLocal的key得到数据源
 */

// 在访问数据库前会调用该类的 determineCurrentLookupKey() 方法获取数据库实例的 key
public class DataSourceRouter extends AbstractRoutingDataSource {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected Object determineCurrentLookupKey() {
        log.info(" 当前数据源: " + DataSourceContextHolder.getCurrentDataSource());
        return DataSourceContextHolder.getCurrentDataSource();
    }


}