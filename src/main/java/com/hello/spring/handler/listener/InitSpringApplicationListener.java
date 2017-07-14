package com.hello.spring.handler.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
@Component
public class InitSpringApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		System.out.println("SpringContext加载完成了！！！！！！！！");
	}

}
