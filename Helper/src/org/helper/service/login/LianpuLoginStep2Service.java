package org.helper.service.login;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.helper.service.BaseService;
import org.helper.util.HelperConstants;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class LianpuLoginStep2Service extends BaseService {

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		return null;
	}

	/**
	 * http://www.lianpunet.com/userapp.php?id=1021978
	 * 
	 * @param farmAppUrl
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws IOException
	 * @throws ParserException
	 */
	public String step2GetFastLoginUrl(String farmAppUrl)
			throws org.apache.http.ParseException, IOException, ParserException {
		setUrl("http://www.lianpunet.com/" + farmAppUrl);
		HttpResponse response = doGet();
		String responseBody = EntityUtils.toString(response.getEntity(),
				HelperConstants.ENCODING_GBK);
		Parser parser = new Parser(responseBody);
		NodeFilter filter = new TagNameFilter("a");
		NodeList linkNodes = parser.extractAllNodesThatMatch(filter);
		if (null != linkNodes) {
			for (int i = 0; i < linkNodes.size(); i++) {
				LinkTag aTag = (LinkTag) linkNodes.elementAt(i);
				if ("登录".equals(aTag.getLinkText())) {
					return aTag.getAttribute("href");
				}
			}
		}
		return "";
	}
}
