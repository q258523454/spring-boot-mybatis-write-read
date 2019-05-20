package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: zhangjian
 * @Date: 2019-05-10
 * @Version 1.0
 */

// @EnableTransactionManagement(order = 2)    // 设置事务执行顺序(需要在切换数据源aop切换之后，否则只走主库)
@EnableTransactionManagement    // 默认Order很低, 将切换数据源的aop数字写小(越小优先级越大)就可以
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) // 禁用掉默认的数据源获取方式，默认会读取配置文件的据源（spring.datasource.*）
@MapperScan("com.dao") // mybatis-dao层
public class SpringBootMybatisWriteReadApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootMybatisWriteReadApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringBootMybatisWriteReadApplication.class);
    }
}
