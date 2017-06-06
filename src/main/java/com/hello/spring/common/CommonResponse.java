package com.hello.spring.common;

import com.hello.spring.util.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jingmin.xiao on 2017/6/6.
 * 通用返回前台Json格式
 */
public class CommonResponse {

    private boolean success;

    private Object message;

    public CommonResponse(){

    }

    public CommonResponse(boolean success, Object message){
        this.success = success;
        this.message = message;
    }

    public CommonResponse(boolean success, Object... args){
        this.success = success;
        this.message = MapUtils.asMap(args);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", success);
        map.put("msg", message);
        return map;
    }
}
