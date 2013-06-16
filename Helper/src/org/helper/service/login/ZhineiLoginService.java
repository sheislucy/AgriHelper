package org.helper.service.login;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.helper.domain.login.ZhineiLoginDomain;
import org.helper.domain.login.ZhineiResponseDomain;
import org.helper.service.ServiceFactory;
import org.htmlparser.util.ParserException;
import org.json.simple.parser.ParseException;

public class ZhineiLoginService {
	public ZhineiResponseDomain loginPhase1(String userName, String password)
			throws ParserException, HttpException, IOException, ParseException {
		ZhineiLoginStep1Service step1 = ServiceFactory
				.getService(ZhineiLoginStep1Service.class);
		ZhineiLoginStep2Service step2 = ServiceFactory
				.getService(ZhineiLoginStep2Service.class);
		ZhineiLoginDomain loginDomain = step1.step1GetMethod();
		return step2.step2PostMethod(loginDomain, userName, password);
	}

	public void loginPhase2(String url) throws ClientProtocolException, IOException {
		ZhineiLoginStep3Service step3 = ServiceFactory.getService(ZhineiLoginStep3Service.class);
		step3.step3GetMethod(url);
	}
}
