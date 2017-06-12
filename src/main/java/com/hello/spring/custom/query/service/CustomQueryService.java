package com.hello.spring.custom.query.service;

import com.hello.spring.custom.query.bean.GridDataBean;
import com.hello.spring.custom.query.common.QueryTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * Created by jingmin.xiao on 2017/6/12.
 */
public interface CustomQueryService {

    /** 获取数据方法名称 */
    String GET_RECORDS_SUFFIX = "getRecords";
    /** 获取记录数名称 */
    String GET_TOTALS_SUFFIX = "getTotals";

    /**
     * 自定义查询列表数据
     * @param queryTypeEnum 查询类型
     * @param params        参数集合
     * @param nameSpace     命名空间
     * @return
     */
    GridDataBean query(QueryTypeEnum queryTypeEnum, String nameSpace, Map<String, Object> params);

    /**
     * 自定义查询
     * @param nameSpace     命名空间
     * @param methodSuffix  方法后缀
     * @param params        参数集合
     * @return
     */
    List<?> query(String nameSpace, String methodSuffix, Map<String, Object> params);
}
