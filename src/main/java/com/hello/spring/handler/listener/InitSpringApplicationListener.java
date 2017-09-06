package com.hello.spring.handler.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
@Component
public class InitSpringApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	private static Logger LOGGER = LoggerFactory.getLogger(InitSpringApplicationListener.class);

	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Spring上下文加载刷新事件");
		}
	}

}
