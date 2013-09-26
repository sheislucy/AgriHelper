/**
 * 
 */
package org.helper.service.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.helper.domain.login.VeryCDResponseDomain;
import org.helper.service.BaseService;
import org.helper.util.CookieSplitter;
import org.helper.util.HttpResponseStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 * 
 */
public class VeryCDLoginService extends BaseService {
	Logger log = LoggerFactory.getLogger(getClass());

	public VeryCDResponseDomain login(String userId, String password)
			throws HttpException, IOException, ParseException {
		// setUrl("http://www.verycd.com/signin");
		setUrl("http://secure.verycd.com/signin");
		Map<String, String> loginParam = new HashMap<String, String>();
		loginParam.put("username", userId);
		loginParam.put("password", password);
		setFormParamMap(loginParam);
		HttpResponse response = doPost();
		int statusCode = response.getStatusLine().getStatusCode();
		String responseBody = EntityUtils.toString(response.getEntity());
		JSONObject json = (JSONObject) new JSONParser().parse(responseBody);
		if (json.get("status") instanceof java.lang.String) {
			if (statusCode == HttpStatus.SC_OK
					&& ((String) json.get("status")).equalsIgnoreCase("ok")) {
				CookieSplitter.splitLoginForVC(response
						.getHeaders("Set-Cookie"));
				return new VeryCDResponseDomain(HttpResponseStatus.SUCCESS);
			}
		} else if (json.get("status") instanceof java.lang.Boolean
				&& !(Boolean) json.get("status")) {
			return new VeryCDResponseDomain(HttpResponseStatus.ERROR,
					(String) json.get("msg"), (String) json.get("info"));
		}
		return new VeryCDResponseDomain();
	}

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		List<BasicHeader> headers = new ArrayList<BasicHeader>();
		headers.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
		return headers;
	}

}
