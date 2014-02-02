package org.helper.service;

import java.io.IOException;
import java.util.ArrayList;
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

	public String step2GetMethod(String url) throws ClientProtocolException, IOException, ParserException {
		setUrl(url);
		HttpResponse response = doGet();
		String responseBody = EntityUtils.toString(response.getEntity(), (UserDomain.getInstance().isVeryCD() ? HelperConstants.ENCODING_UTF8
				: HelperConstants.ENCODING_GBK));
		Parser parser = new Parser(responseBody);
		NodeFilter filter = new TagNameFilter("iframe");
		NodeList nodes = parser.extractAllNodesThatMatch(filter);
		if (null != nodes) {
			for (int i = 0; i < nodes.size(); i++) {
				TagNode frameTag = (TagNode) nodes.elementAt(i);
				if (("app1021978_").equalsIgnoreCase(frameTag.getAttribute("id"))) {
					return frameTag.getAttribute("src").replaceAll("&amp;", "&").replace("happyfarm.manyou-apps.com?", "happyfarm.manyou-apps.com/?");
				}
			}
		}
		return "";
	}

	@Override
	protected List<BasicHeader> buildCommonHeaders() {
		List<BasicHeader> headers = new ArrayList<BasicHeader>();
		headers.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"));
		headers.add(new BasicHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36"));
		if (UserDomain.getInstance().isVeryCD()) {
			headers.add(new BasicHeader("Referer", "http://home.verycd.com/userapp.php?id=1021978&my_suffix=Lw%3D%3D"));
		} else if (UserDomain.getInstance().isZhinei()) {
			headers.add(new BasicHeader("Referer", "http://my.zhinei.com/userapp.php?id=1021978&my_suffix=Lw%3D%3D"));
		} else if (UserDomain.getInstance().isLianpen()) {
			headers.add(new BasicHeader("Referer", "http://www.lianpunet.com/userapp.php?id=1021978&my_suffix=Lw%3D%3D"));
		}
		return headers;
	}
}
