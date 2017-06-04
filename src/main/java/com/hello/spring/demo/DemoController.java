package com.hello.spring.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.HashMap;
/**
 * Created by Administrator on 2017-06-04.
 */
@Controller("demoController")
@RequestMapping("demo")
public class DemoController {

    @ResponseBody
    @RequestMapping("sayHello")
    public Object sayHello(@RequestParam Map<String, Object> params){

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", "HelloSpring");
        System.out.println(map);

        return map;
    }
}
