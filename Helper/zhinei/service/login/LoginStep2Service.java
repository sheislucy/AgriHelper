package org.helper.service.login;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.helper.domain.login.ZhineiLoginDomain;
import org.helper.domain.login.ZhineiResponseDomain;
import org.helper.service.BaseService;
import org.helper.util.CookieSplitter;
import org.helper.util.HelperLoggerAppender;
import org.helper.util.HttpResponseStatus;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class LoginStep2Service extends BaseService {

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		List<BasicHeader> headers = new ArrayList<BasicHeader>();
		headers.add(new BasicHeader("Host", "my.zhinei.com"));
		headers.add(new BasicHeader("Content-Type",
				"application/x-www-form-urlencoded"));
		headers.add(new BasicHeader("Referer", "http://my.zhinei.com/index.php"));
		return headers;
	}

	public ZhineiResponseDomain step2PostMethod(ZhineiLoginDomain loginDomain,
			String userName, String password) throws ClientProtocolException,
			IOException, ParserException {
		setUrl("http://my.zhinei.com/" + loginDomain.getLoginUrl());
		Map<String, String> loginParam = new HashMap<String, String>();
		loginParam.put("username", userName);
		loginParam.put("login_type", loginDomain.getLoginType());
		loginParam.put("password", password);
		loginParam.put("formhash", loginDomain.getFormHash());
		loginParam.put("loginsubmit",
				URLEncoder.encode(loginDomain.getLoginSubmit(), "gbk"));
		setFormParamMap(loginParam);

		HttpResponse response = doPost();
		String responseBody = EntityUtils.toString(response.getEntity(), "gbk");
		Parser parser = new Parser(responseBody);
		NodeList nodes = parser.extractAllNodesThatMatch(new TagNameFilter(
				"div"));
		if (null != nodes) {
			for (int i = 0; i < nodes.size(); i++) {
				TagNode divTag = (TagNode) nodes.elementAt(i);
				if ("notice".equalsIgnoreCase(divTag.getAttribute("class"))) {
					ZhineiResponseDomain responseDomain = new ZhineiResponseDomain();
					if (divTag.getFirstChild() instanceof TagNode) {
						TagNode aNode = (TagNode) divTag.getFirstChild();
						if ("space-home.html".equalsIgnoreCase(aNode
								.getAttribute("href"))) {
							NodeList scriptNodes = aNode.getChildren();
							responseDomain
									.setLogin3Url(((ScriptTag) scriptNodes
											.elementAt(1)).getAttribute("src"));
							responseDomain
									.setStatus(HttpResponseStatus.SUCCESS);
							responseDomain.setInfoText(aNode
									.toPlainTextString());
							CookieSplitter.splitLogin(response
									.getHeaders("Set-Cookie"));
							HelperLoggerAppender.writeLog(aNode
									.toPlainTextString());
						} else {
							responseDomain.setStatus(HttpResponseStatus.ERROR);
							responseDomain.setInfoText(aNode
									.toPlainTextString());
						}
					} else {

						responseDomain.setStatus(HttpResponseStatus.ERROR);
						responseDomain.setInfoText(divTag.toPlainTextString());
					}
					return responseDomain;
				}
			}
		}
		return null;
	}
}
