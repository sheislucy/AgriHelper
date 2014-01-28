package org.helper.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.helper.domain.login.UserDomain;
import org.helper.util.HelperConstants;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class RefreshFarmStep2Service extends BaseService {

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		return null;
	}

	public String step2GetMethod(String url) throws ClientProtocolException,
			IOException, ParserException {
		setUrl(url);
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
				if (("app1021978_").equalsIgnoreCase(frameTag
						.getAttribute("id"))) {
					return frameTag
							.getAttribute("src")
							.replaceAll("&amp;", "&")
							.replace("happyfarm.manyou-apps.com?",
									"happyfarm.manyou-apps.com/?");
				}
			}
		}
		return "";
	}

}
