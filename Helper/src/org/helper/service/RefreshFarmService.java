package org.helper.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.htmlparser.util.ParserException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class RefreshFarmService {
	protected final static ApplicationContext ctx = new ClassPathXmlApplicationContext(
			"applicationContext.xml");

	public void refresh() {
		RefreshFarmStep1Service step1 = ctx
				.getBean(RefreshFarmStep1Service.class);
		try {
			step1.step1GetMethod();
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
