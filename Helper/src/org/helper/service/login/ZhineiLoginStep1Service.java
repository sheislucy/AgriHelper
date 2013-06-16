/**
 * 
 */
package org.helper.service.login;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.helper.domain.login.ZhineiLoginDomain;
import org.helper.service.BaseService;
import org.helper.util.HelperConstants;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 * 
 */
public class ZhineiLoginStep1Service extends BaseService {
	Logger log = LoggerFactory.getLogger(getClass());

	public ZhineiLoginDomain step1GetMethod() throws HttpException,
			IOException, ParseException, ParserException {
		setUrl("http://my.zhinei.com/index.php");
		HttpResponse response = doGet();
		String responseBody = EntityUtils.toString(response.getEntity(),
				HelperConstants.ENCODING_GBK);
		Parser parser = new Parser(responseBody);
		NodeFilter filter = new TagNameFilter("form");
		NodeList formNodes = parser.extractAllNodesThatMatch(filter);
		if (null != formNodes) {
			for (int i = 0; i < formNodes.size(); i++) {
				TagNode formTag = (TagNode) formNodes.elementAt(i);
				if (("loginform").equalsIgnoreCase(formTag.getAttribute("id"))) {
					ZhineiLoginDomain loginDomain = new ZhineiLoginDomain();
					loginDomain.setLoginUrl(formTag.getAttribute("action"));
					NodeList formChildren = formTag.getChildren();
					NodeList inputNodes = formChildren
							.extractAllNodesThatMatch(
									new TagNameFilter("input"), true);
					for (int j = 0; j < inputNodes.size(); j++) {
						TagNode inputTag = (TagNode) inputNodes.elementAt(j);
						if (("formhash").equalsIgnoreCase(inputTag
								.getAttribute("name"))) {
							loginDomain.setFormHash(inputTag
									.getAttribute("value"));
						} else if (("loginsubmit").equalsIgnoreCase(inputTag
								.getAttribute("name"))) {
							loginDomain.setLoginSubmit(inputTag
									.getAttribute("value"));
						} else if (("login_type").equalsIgnoreCase(inputTag
								.getAttribute("name"))) {
							loginDomain.setLoginType(inputTag
									.getAttribute("value"));
						}
					}
					return loginDomain;
				}
			}
		}
		return null;
	}

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		return null;
	}

}
