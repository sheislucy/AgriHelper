/**
 * 
 */
package org.helper.service;

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
import org.helper.util.CookieSplitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * 
 */
@Service
public class LoginService extends BaseService {
	Logger log = LoggerFactory.getLogger(getClass());

	public void login(String userId, String password) throws HttpException,
			IOException {
		setUrl("http://www.verycd.com/signin");
		Map<String, String> loginParam = new HashMap<String, String>();
		loginParam.put("username", userId);
		loginParam.put("password", password);
		setFormParamMap(loginParam);
		HttpResponse response = doPost();
		int statusCode = response.getStatusLine().getStatusCode();
		EntityUtils.toString(response.getEntity());
		if (statusCode == HttpStatus.SC_OK
				|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
			CookieSplitter.split(response.getHeaders("Set-Cookie"));
		}
	}


	@Override
	protected List<BasicHeader> extendRequestHeader() {
		List<BasicHeader> headers = new ArrayList<BasicHeader>();
		headers.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
		return headers;
	}

}
