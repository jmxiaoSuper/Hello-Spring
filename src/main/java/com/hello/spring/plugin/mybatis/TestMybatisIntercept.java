package com.hello.spring.plugin.mybatis;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Properties;

/**
 * Created by Super.Xjm on 2017/9/6.
 * Mybatis拦截器
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class TestMybatisIntercept implements Interceptor {
    final static Logger LOGGER = LoggerFactory.getLogger(TestMybatisIntercept.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("进入Mybatis拦截器 %s( %s )", invocation.getMethod().getName(), invocation.getArgs());
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}

