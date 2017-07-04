package com.hello.spring.custom.query.common;

import java.util.HashMap;
import java.util.Map;

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

    private static Map<String, QueryTypeEnum> map = new HashMap<String, QueryTypeEnum>();
    static{
        QueryTypeEnum[] _arr = QueryTypeEnum.values();
        for (int i = 0, _len = _arr.length;  i < _len; i++) {
            QueryTypeEnum temp = _arr[i];
            map.put(temp.type, temp);
        }
    }

    public static QueryTypeEnum getType(String value){
        if(value == null){
            throw new IllegalArgumentException("枚举类 value不能为空！");
        }
        if ( map.containsKey(value)){
            return map.get(value);
        }else{
            throw new IllegalArgumentException("QueryTypeEnum : 不支持的value值！");
        }
    }
}
