package com.hello.spring.demo;

/**
 * Created by Administrator on 2017-06-04.
 */
public class ss {
    private static ss ourInstance = new ss();

    public static ss getInstance() {
        return ourInstance;
    }

    private ss() {
    }
}
