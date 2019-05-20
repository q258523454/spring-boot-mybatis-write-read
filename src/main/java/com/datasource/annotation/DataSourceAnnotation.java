package com.datasource.annotation;

import com.datasource.entity.DataSourceEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据源选择--自定义注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataSourceAnnotation {

    DataSourceEnum value() default DataSourceEnum.MASTER;    // 默认主表master

}
