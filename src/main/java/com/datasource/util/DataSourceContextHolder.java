package com.datasource.util;

import com.datasource.entity.DataSourceEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态数据源的上下文 threadlocal
 */
public class DataSourceContextHolder {
    private final static ThreadLocal<String> local = new ThreadLocal<>();

    public static void putDataSource(String name) {
        local.set(name);
    }

    public static String getCurrentDataSource() {
        return local.get();
    }

    public static void removeCurrentDataSource() {
        local.remove();
    }

}