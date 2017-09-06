package com.hello.spring.helper;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Created by Super.Xjm on 2017/9/6.
 * Web生命周期关键上下文环境获取帮助类
 */
@Service
public class SpringHelper implements ApplicationContextAware, ServletContextAware {

    private static ApplicationContext applicationContext;

    private static ServletContext servletContext;

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static ServletContext getServletContext(){
        return servletContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    private SpringHelper(){}
}
