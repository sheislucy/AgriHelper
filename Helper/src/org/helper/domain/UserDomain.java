package org.helper.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserDomain implements Serializable {
	private static final long serialVersionUID = -8608225081404150735L;

	private Map<String, Object> cookieMap = new HashMap<String, Object>();

	private static UserDomain userDomain;

	private UserDomain() {
	}

	public static UserDomain getInstance() {
		if (null == userDomain || null == userDomain) {
			userDomain = new UserDomain();
		}
		return userDomain;
	}

	public void addCookie(String key, Object value) {
		cookieMap.put(key, value);
	}

	public void addCookies(Map<String, Object> cookies) {
		cookieMap.putAll(cookies);
	}

	public void setCookies(Map<String, Object> cookies) {
		cookieMap = cookies;
	}
}
