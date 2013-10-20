package org.helper.service.login;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.helper.domain.login.UserDomain;
import org.helper.enums.EmCookieKeys;
import org.helper.service.BaseService;
import org.helper.util.CookieSplitter;

public class ZhineiLoginStep3Service extends BaseService {

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		return null;
	}

	public void step3GetMethod(String url) throws ClientProtocolException,
			IOException {
		setUrl(url);
		HttpResponse response = doGet();
		CookieSplitter.splitLoginForZN(response.getHeaders("Set-Cookie"));
	}

	@Override
	protected CookieStore buildCookieStore() {
		BasicCookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie(
				EmCookieKeys.ZHINEI_LOGINUSER.getValue(),
				(String) (UserDomain.getInstance()
						.getCookieValue(EmCookieKeys.ZHINEI_LOGINUSER
								.getValue())));
		BasicClientCookie cookie2 = new BasicClientCookie(
				EmCookieKeys.ZHINEI_AUTH.getValue(),
				(String) (UserDomain.getInstance()
						.getCookieValue(EmCookieKeys.ZHINEI_AUTH.getValue())));
		cookie.setDomain(".zhinei.com");
		cookie.setPath("/");
		cookie2.setDomain(".zhinei.com");
		cookie2.setPath("/");
		cookieStore.addCookie(cookie);
		cookieStore.addCookie(cookie2);
		return cookieStore;
	}
}
