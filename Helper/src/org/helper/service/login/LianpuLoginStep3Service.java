package org.helper.service.login;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.helper.domain.login.LianpuLoginDomain;
import org.helper.service.BaseService;
import org.helper.util.HelperConstants;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class LianpuLoginStep3Service extends BaseService {

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		return null;
	}

	/**
	 * http://www.lianpunet.com/do.php?ac=2362c6386a4eee615ac142c2013a9fbd
	 * 
	 * @param fastLoginUrl
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws IOException
	 * @throws ParserException
	 */
	public LianpuLoginDomain step3GetLianpuLoginDomain(String fastLoginUrl, String refer) throws org.apache.http.ParseException, IOException,
			ParserException {
		setUrl("http://www.lianpunet.com/" + fastLoginUrl);
		HttpResponse response = doGet();
		String responseBody = EntityUtils.toString(response.getEntity(), HelperConstants.ENCODING_GBK);
		Parser parser = new Parser(responseBody);
		NodeFilter filter = new TagNameFilter("form");
		NodeList formNodes = parser.extractAllNodesThatMatch(filter);
		if (null != formNodes) {
			for (int i = 0; i < formNodes.size(); i++) {
				TagNode formTag = (TagNode) formNodes.elementAt(i);
				if (("loginform").equalsIgnoreCase(formTag.getAttribute("id"))) {
					LianpuLoginDomain loginDomain = new LianpuLoginDomain();
					loginDomain.setLoginUrl(formTag.getAttribute("action"));
					NodeList formChildren = formTag.getChildren();
					NodeList inputNodes = formChildren.extractAllNodesThatMatch(new TagNameFilter("input"), true);
					for (int j = 0; j < inputNodes.size(); j++) {
						TagNode inputTag = (TagNode) inputNodes.elementAt(j);
						if (("formhash").equalsIgnoreCase(inputTag.getAttribute("name"))) {
							loginDomain.setFormHash(inputTag.getAttribute("value"));
						} else if (("loginsubmit").equalsIgnoreCase(inputTag.getAttribute("name"))) {
							loginDomain.setLoginSubmit(inputTag.getAttribute("value"));
						} else if (("cookietime").equalsIgnoreCase(inputTag.getAttribute("name"))) {
							loginDomain.setCookieTime(inputTag.getAttribute("value"));
						} else if (("refer").equalsIgnoreCase(inputTag.getAttribute("name"))) {
							loginDomain.setRefer(inputTag.getAttribute("value"));
						}
					}
					// loginDomain.setRefer(refer);
					return loginDomain;
				}
			}
		}
		return null;
	}
}
