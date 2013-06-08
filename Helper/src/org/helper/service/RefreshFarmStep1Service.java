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
import org.helper.domain.UserDomain;
import org.helper.util.EmCookieKeys;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class RefreshFarmStep1Service extends BaseService {

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		List<BasicHeader> headers = new ArrayList<BasicHeader>();
		headers.add(new BasicHeader("id", "1021978"));
		headers.add(new BasicHeader("my_suffix", "Lw%3D%3D"));
		headers.add(new BasicHeader("Host", "home.verycd.com"));
		return headers;
	}

	public String step1GetMethod() throws ClientProtocolException, IOException,
			ParserException {
		setUrl("http://home.verycd.com/userapp.php");
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
				.append((String) UserDomain.getInstance().getCookieValue(
						EmCookieKeys.SID.getValue().toUpperCase()))
				.append("; member_id=")
				.append((String) UserDomain.getInstance().getCookieValue(
						EmCookieKeys.MEMBER_ID.getValue().toUpperCase()))
				.append("; member_name=")
				.append((String) UserDomain.getInstance().getCookieValue(
						EmCookieKeys.MEMBER_NAME.getValue().toUpperCase()))
				.append("; mgroupId=")
				.append((String) UserDomain.getInstance().getCookieValue(
						EmCookieKeys.M_GROUP_ID.getValue().toUpperCase()))
				.append("; pass_hash=")
				.append((String) UserDomain.getInstance().getCookieValue(
						EmCookieKeys.PASS_HASH.getValue().toUpperCase()))
				.append("; uchome_auth=")
				.append((String) UserDomain.getInstance().getCookieValue(
						EmCookieKeys.UCHOME_AUTH.getValue().toUpperCase()))
				.append("; uchome_loginuser=")
				.append((String) UserDomain.getInstance().getCookieValue(
						EmCookieKeys.UCHOME_LOGINUSER.getValue().toUpperCase()))
				.append(";");

		BasicClientCookie cookie = new BasicClientCookie("",
				cookieValue.toString());
		cookie.setDomain("verycd.com");
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		return cookieStore;
	}
}
