package com.hello.spring.demo.dao;

import com.hello.spring.demo.entity.Demo;
import org.springframework.stereotype.Repository;

/**
 * Created by Super.Xjm on 2017/7/4.
 */
@Repository
public interface TestMapper {

   Demo selectByPrimaryKey(int id);

   int deleteByPrimaryKey(int id);

   int insert(Demo demo);
}
