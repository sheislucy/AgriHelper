package org.helper.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.helper.util.EmCookieKeys;

public class UserDomain implements Serializable {
	private static final long serialVersionUID = -8608225081404150735L;

	private Map<String, Object> cookieMap = new HashMap<String, Object>();

	private static ThreadLocal<UserDomain> userDomain;

	private UserDomain() {
	}

	public static UserDomain getInstance() {
		if (null == userDomain || null == userDomain.get()) {
			userDomain = new ThreadLocal<UserDomain>();
			userDomain.set(new UserDomain());
		}
		return userDomain.get();
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

	public Object getCookieValue(String cookieName) {
		return cookieMap.get(cookieName);
	}

	public boolean isLoggedIn(String name) {
		return ((String) cookieMap.get(EmCookieKeys.MEMBER_NAME.getValue()
				.toUpperCase())).equalsIgnoreCase(name);
	}
}
