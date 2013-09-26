package org.helper.service.login;

import java.io.IOException;

import org.apache.http.ParseException;
import org.helper.domain.login.LianpuLoginDomain;
import org.helper.domain.login.LianpuResponseDomain;
import org.helper.service.ServiceFactory;
import org.htmlparser.util.ParserException;

public class LianpuLoginService {
	public LianpuResponseDomain login(String username, String password)
			throws ParseException, ParserException, IOException {
		LianpuLoginStep1Service step1 = ServiceFactory
				.getService(LianpuLoginStep1Service.class);
		LianpuLoginStep2Service step2 = ServiceFactory
				.getService(LianpuLoginStep2Service.class);
		LianpuLoginStep3Service step3 = ServiceFactory
				.getService(LianpuLoginStep3Service.class);
		LianpuLoginStep4Service step4 = ServiceFactory
				.getService(LianpuLoginStep4Service.class);
		String farmUrl = step1.step1GetFarmAppUrl();
		LianpuLoginDomain domain = step3.step3GetLianpuLoginDomain(
				step2.step2GetFastLoginUrl(farmUrl), farmUrl);
		return step4.storeCookieForLianpu(domain, username, password);
	}
}
