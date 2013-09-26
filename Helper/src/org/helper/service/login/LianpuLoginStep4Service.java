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
import org.helper.domain.login.LianpuLoginDomain;
import org.helper.domain.login.LianpuResponseDomain;
import org.helper.service.BaseService;
import org.helper.util.CookieSplitter;
import org.helper.util.HelperConstants;
import org.helper.util.HttpResponseStatus;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class LianpuLoginStep4Service extends BaseService {

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		List<BasicHeader> headers = new ArrayList<BasicHeader>();
		headers.add(new BasicHeader("Host", "www.lianpunet.com"));
		headers.add(new BasicHeader("Content-Type",
				"application/x-www-form-urlencoded"));
		headers.add(new BasicHeader("Referer", getUrl()));
		return headers;
	}

	/**
	 * POST
	 * http://www.lianpunet.com/do.php?ac=2362c6386a4eee615ac142c2013a9fbd&&ref
	 * 
	 * @param loginDomain
	 * @param userName
	 * @param password
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws ParserException
	 */
	public LianpuResponseDomain storeCookieForLianpu(
			LianpuLoginDomain loginDomain, String userName, String password)
			throws ClientProtocolException, IOException, ParserException {
		setUrl("http://www.lianpunet.com/" + loginDomain.getLoginUrl());
		Map<String, String> loginParam = new HashMap<String, String>();
		loginParam.put("username", userName);
		loginParam.put("cookietime", loginDomain.getCookieTime());
		loginParam.put("password", password);
		loginParam.put("formhash", loginDomain.getFormHash());
		loginParam.put("refer", loginDomain.getRefer());
		loginParam.put("loginsubmit", URLEncoder.encode(
				loginDomain.getLoginSubmit(), HelperConstants.ENCODING_GBK));
		setFormParamMap(loginParam);

		HttpResponse response = doPost();
		String responseBody = EntityUtils.toString(response.getEntity(),
				HelperConstants.ENCODING_GBK);
		Parser parser = new Parser(responseBody);
		NodeList nodes = parser.extractAllNodesThatMatch(new TagNameFilter(
				"div"));
		if (null != nodes) {
			for (int i = 0; i < nodes.size(); i++) {
				TagNode divTag = (TagNode) nodes.elementAt(i);
				if ("showmessage"
						.equalsIgnoreCase(divTag.getAttribute("class"))) {
					LianpuResponseDomain reponse = new LianpuResponseDomain();
					NodeList divChildren = divTag.getChildren();
					NodeList aTag = divChildren.extractAllNodesThatMatch(
							new TagNameFilter("a"), true);
					if (aTag.size() > 0) {
						LinkTag firstA = (LinkTag) aTag.elementAt(0);
						if (firstA.getAttribute("href").contains("userapp")) {
							reponse.setInfoText(((LinkTag) aTag.elementAt(0))
									.getLinkText());
							reponse.setStatus(HttpResponseStatus.SUCCESS);
							CookieSplitter.splitLoginForLP(response
									.getHeaders("Set-Cookie"));
							return reponse;
						} else {
							reponse.setInfoText(((LinkTag) aTag.elementAt(0))
									.getLinkText());
							reponse.setStatus(HttpResponseStatus.ERROR);
							return reponse;
						}
					}
				}
			}
		}

		return new LianpuResponseDomain();
	}
}
