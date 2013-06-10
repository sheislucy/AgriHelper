package org.helper.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class HelperAppContext {
	public final static ApplicationContext CTX = new ClassPathXmlApplicationContext(
			"applicationContext.xml");
}
