package com.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.datasource.router.DataSourceRouter;
import com.datasource.entity.DataSourceEnum;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置
 */
@Configuration
public class DataSourceConfig {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public final static String masterTransactionManager = "masterTransactionManager";

    public final static String slaveTransactionManager = "slaveTransactionManager";

    /***
     * 注意这里用的 Druid 连接池
     */
    @Bean(name = "dbMaster")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource dbMaster() {
        log.info("master数据源");
        return new DruidDataSource();
    }

    @Bean(name = "dbSlave")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource dbSlave() {
        log.info("slave数据源");
        return new DruidDataSource();
    }


    /***
     * @Primary： 相同的bean中，优先使用用@Primary注解的bean.
     * @Qualifier:： 这个注解则指定某个bean有没有资格进行注入。
     */
    @Primary
    @Bean(name = "dataSourceRouter") // 对应Bean: DataSourceRouter
    public DataSource dataSourceRouter(@Qualifier("dbMaster") DataSource master, @Qualifier("dbSlave") DataSource slave) {
        DataSourceRouter dataSourceRouter = new DataSourceRouter();
        log.info(" ---------------------- 德鲁伊配置信息 BEGIN----------------------");
        DruidDataSource druidDataSourceMaster = (DruidDataSource) master;
        DruidDataSource druidDataSourceSlave = (DruidDataSource) slave;
        log.info("master: ");
        log.info("检测连接是否有效的sql: " + druidDataSourceMaster.getValidationQuery());
        log.info("最小空闲连接池数量: " + druidDataSourceMaster.getMinIdle());
        log.info("removeAbandoned功能: " + druidDataSourceMaster.removeAbandoned());
        log.info("超过时间限制时间(单位秒): " + druidDataSourceMaster.getRemoveAbandonedTimeout());
        log.info("slave: ");
        log.info("检测连接是否有效的sql: " + druidDataSourceSlave.getValidationQuery());
        log.info("最小空闲连接池数量: " + druidDataSourceSlave.getMinIdle());
        log.info("removeAbandoned功能: " + druidDataSourceSlave.removeAbandoned());
        log.info("超过时间限制时间(单位秒): " + druidDataSourceSlave.getRemoveAbandonedTimeout());
        log.info(" ---------------------- 德鲁伊配置信息 END----------------------");

        //配置多数据源
        Map<Object, Object> map = new HashMap<>(5);
        map.put(DataSourceEnum.MASTER.getName(), master);    // key需要跟ThreadLocal中的值对应
        map.put(DataSourceEnum.SLAVE.getName(), slave);
        // master 作为默认数据源
        dataSourceRouter.setDefaultTargetDataSource(master);
        dataSourceRouter.setTargetDataSources(map);
        return dataSourceRouter;
    }

    // 注入动态数据源 DataSourceTransactionManager 用于事务管理(事务回滚只针对同一个数据源)
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("dataSourceRouter") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
