package com.hello.spring.custom.query.common;

/**
 * Created by jingmin.xiao on 2017/6/12.
 */
public enum QueryTypeEnum {

    ONLY_RECORDS("1"),

    ONLY_TOTALS("2"),

    ALL("3");

    private String type;

    QueryTypeEnum(String type){
        this.type = type;
    }

}
