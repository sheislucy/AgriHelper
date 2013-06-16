package org.helper.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.helper.domain.login.ZhineiUserDomain;
import org.helper.util.EmCookieKeys;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class RefreshFarmStep1Service extends BaseService {

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		return null;
	}

	public String step1GetMethod() throws ClientProtocolException, IOException,
			ParserException {
		setUrl("http://my.zhinei.com/userapp.php?id=1021978&my_suffix=Lw==");
		HttpResponse response = doGet();
		String responseBody = EntityUtils.toString(response.getEntity(),
				"utf-8");
		Parser parser = new Parser(responseBody);
		NodeFilter filter = new TagNameFilter("iframe");
		NodeList nodes = parser.extractAllNodesThatMatch(filter);
		if (null != nodes) {
			for (int i = 0; i < nodes.size(); i++) {
				TagNode frameTag = (TagNode) nodes.elementAt(i);
				if (("ifm0").equalsIgnoreCase(frameTag.getAttribute("id"))) {
					return frameTag.getAttribute("src");
				}
			}
		}
		return "";
	}

	@Override
	protected CookieStore buildCookieStore() {
		BasicCookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie(
				EmCookieKeys.ZHINEI_LOGINUSER.getValue(),
				(String) (ZhineiUserDomain.getInstance()
						.getCookieValue(EmCookieKeys.ZHINEI_LOGINUSER
								.getValue())));
		BasicClientCookie cookie2 = new BasicClientCookie(
				EmCookieKeys.ZHINEI_AUTH.getValue(),
				(String) (ZhineiUserDomain.getInstance()
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
