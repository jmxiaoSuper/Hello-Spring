package com.hello.spring.custom.query.service.impl;

import com.hello.spring.custom.query.bean.GridDataBean;
import com.hello.spring.custom.query.common.QueryTypeEnum;
import com.hello.spring.custom.query.service.CustomQueryService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.springframework.util.Assert.notNull;

/**
 * Created by jingmin.xiao on 2017/6/12.
 */
@Service
public class CustomQueryServiceImpl implements CustomQueryService{
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public GridDataBean query(QueryTypeEnum queryTypeEnum, String nameSpace, Map<String, Object> params) {
        SqlSession session = null;
        List<?> items = null;
        int totals = 0;

        try{
            session = SqlSessionUtils.getSqlSession(sqlSessionFactory);

            if(queryTypeEnum == QueryTypeEnum.ALL || queryTypeEnum == QueryTypeEnum.ONLY_RECORDS){
                items = session.selectList(nameSpace + CustomQueryService.GET_RECORDS_SUFFIX, params);
            }
            if(queryTypeEnum == QueryTypeEnum.ALL || queryTypeEnum == QueryTypeEnum.ONLY_TOTALS){
                totals = session.selectOne(nameSpace + CustomQueryService.GET_TOTALS_SUFFIX, params);
            }

        }catch(Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }finally {
            if (session != null) {
                SqlSessionUtils.closeSqlSession(session, sqlSessionFactory);
            }
        }

        return new GridDataBean(totals, items);
    }

    public List<?> query(String nameSpace, String methodSuffix, Map<String, Object> params) {
        SqlSession session = null;
        List<?> items = null;

        try{
            session = SqlSessionUtils.getSqlSession(sqlSessionFactory);
            items = session.selectList(nameSpace + methodSuffix, params);

        }catch(Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }finally {
            if (session != null) {
                SqlSessionUtils.closeSqlSession(session, sqlSessionFactory);
            }
        }
        return items;
    }
}
