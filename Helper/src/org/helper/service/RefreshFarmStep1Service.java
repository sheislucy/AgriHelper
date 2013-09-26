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
import org.helper.domain.login.UserDomain;
import org.helper.util.EmCookieKeys;
import org.helper.util.HelperConstants;
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
		if (UserDomain.getInstance().isVeryCD()) {
			setUrl("http://home.verycd.com/userapp.php?id=1021978&my_suffix=Lw%3D%3D");
		} else if (UserDomain.getInstance().isZhinei()) {
			setUrl("http://my.zhinei.com/userapp.php?id=1021978&my_suffix=Lw==");
		} else if (UserDomain.getInstance().isLianpen()) {
			setUrl("http://www.lianpunet.com/userapp.php?id=1021978&my_suffix=Lw%3D%3D");
		}
		HttpResponse response = doGet();
		String responseBody = EntityUtils
				.toString(response.getEntity(), (UserDomain.getInstance()
						.isVeryCD() ? HelperConstants.ENCODING_UTF8
						: HelperConstants.ENCODING_GBK));
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
		if (UserDomain.getInstance().isVeryCD()) {
			StringBuilder cookieValue = new StringBuilder("sid=");
			cookieValue
					.append((String) UserDomain.getInstance().getCookieValue(
							EmCookieKeys.SID.getValue()))
					.append("; member_id=")
					.append((String) UserDomain.getInstance().getCookieValue(
							EmCookieKeys.MEMBER_ID.getValue()))
					.append("; member_name=")
					.append((String) UserDomain.getInstance().getCookieValue(
							EmCookieKeys.MEMBER_NAME.getValue()))
					.append("; mgroupId=")
					.append((String) UserDomain.getInstance().getCookieValue(
							EmCookieKeys.M_GROUP_ID.getValue()))
					.append("; pass_hash=")
					.append((String) UserDomain.getInstance().getCookieValue(
							EmCookieKeys.PASS_HASH.getValue()))
					.append("; uchome_auth=")
					.append((String) UserDomain.getInstance().getCookieValue(
							EmCookieKeys.UCHOME_AUTH.getValue()))
					.append("; uchome_loginuser=")
					.append((String) UserDomain.getInstance().getCookieValue(
							EmCookieKeys.UCHOME_LOGINUSER.getValue()))
					.append(";");

			BasicClientCookie cookie = new BasicClientCookie("",
					cookieValue.toString());
			cookie.setDomain("verycd.com");
			cookie.setPath("/");
			cookieStore.addCookie(cookie);
		} else if (UserDomain.getInstance().isZhinei()) {
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
		} else if (UserDomain.getInstance().isLianpen()) {
			BasicClientCookie cookie = new BasicClientCookie(
					EmCookieKeys.UCHOME_AUTH.getValue(),
					(String) (UserDomain.getInstance()
							.getCookieValue(EmCookieKeys.UCHOME_AUTH.getValue())));
			BasicClientCookie cookie2 = new BasicClientCookie(
					EmCookieKeys.UCHOME_LOGINUSER.getValue(),
					(String) (UserDomain.getInstance()
							.getCookieValue(EmCookieKeys.UCHOME_LOGINUSER
									.getValue())));
			cookie.setDomain(".lianpunet.com");
			cookie.setPath("/");
			cookie2.setDomain(".lianpunet.com");
			cookie2.setPath("/");
			cookieStore.addCookie(cookie);
			cookieStore.addCookie(cookie2);
		}
		return cookieStore;
	}
}
