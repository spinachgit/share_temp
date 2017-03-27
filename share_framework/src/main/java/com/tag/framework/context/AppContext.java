package com.tag.framework.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class AppContext {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppContext.class);

	private static ApplicationContext context = null;

	public static Object getBean(String beanName) {
		Object obj = context.getBean(beanName);
		return obj;
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public static void setContext(ApplicationContext context) {
		AppContext.context = context;
	}

}