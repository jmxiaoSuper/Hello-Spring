package com.hello.spring.handler.resolver.exception;

import com.hello.spring.common.CommonResponse;
import com.hello.spring.util.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jingmin.xiao on 2017/6/6.
 * 统一异常处理，封装成统一JSON格式
 */
@Service
public class CustomHandlerExceptionResolver implements HandlerExceptionResolver {
    public static final String RESPONSE_ERROR_MESSAGE = "errorText";
    public static final String RESPONSE_ERROR_DETAIL = "errorDetail";

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        CommonResponse retObj = new CommonResponse(false,
                RESPONSE_ERROR_MESSAGE, ex.getMessage(),
                RESPONSE_ERROR_DETAIL, LogUtil.getStackTrace(ex)
        );

    	/* 返回通用的json格式信息 */
        ModelAndView mav = new ModelAndView();
        MappingJacksonJsonView view = new MappingJacksonJsonView();
        view.setAttributesMap(retObj.toMap());
        mav.setView(view);
        return mav;

    }
}
