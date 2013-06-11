package org.helper.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.helper.domain.VeryCDUserDomain;
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
		List<BasicHeader> headers = new ArrayList<BasicHeader>();
		headers.add(new BasicHeader("Host", "home.verycd.com"));
		return headers;
	}

	public String step1GetMethod() throws ClientProtocolException, IOException,
			ParserException {
		setUrl("http://home.verycd.com/userapp.php?id=1021978&my_suffix=Lw%3D%3D");
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
		StringBuilder cookieValue = new StringBuilder("sid=");
		cookieValue
				.append((String) VeryCDUserDomain.getInstance().getCookieValue(
						EmCookieKeys.SID.getValue()))
				.append("; member_id=")
				.append((String) VeryCDUserDomain.getInstance().getCookieValue(
						EmCookieKeys.MEMBER_ID.getValue()))
				.append("; member_name=")
				.append((String) VeryCDUserDomain.getInstance().getCookieValue(
						EmCookieKeys.MEMBER_NAME.getValue()))
				.append("; mgroupId=")
				.append((String) VeryCDUserDomain.getInstance().getCookieValue(
						EmCookieKeys.M_GROUP_ID.getValue()))
				.append("; pass_hash=")
				.append((String) VeryCDUserDomain.getInstance().getCookieValue(
						EmCookieKeys.PASS_HASH.getValue()))
				.append("; uchome_auth=")
				.append((String) VeryCDUserDomain.getInstance().getCookieValue(
						EmCookieKeys.UCHOME_AUTH.getValue()))
				.append("; uchome_loginuser=")
				.append((String) VeryCDUserDomain.getInstance().getCookieValue(
						EmCookieKeys.UCHOME_LOGINUSER.getValue()))
				.append(";");

		BasicClientCookie cookie = new BasicClientCookie("",
				cookieValue.toString());
		cookie.setDomain("verycd.com");
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		return cookieStore;
	}
}
