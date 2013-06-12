package org.helper.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.helper.util.EmCookieKeys;

public class VeryCDUserDomain implements Serializable {
	private static final long serialVersionUID = -8608225081404150735L;

	private Map<String, Object> cookieMap = new HashMap<String, Object>();

	private static ThreadLocal<VeryCDUserDomain> userDomain;

	private VeryCDUserDomain() {
	}

	public static VeryCDUserDomain getInstance() {
		if (null == userDomain || null == userDomain.get()) {
			userDomain = new ThreadLocal<VeryCDUserDomain>();
			userDomain.set(new VeryCDUserDomain());
		}
		return userDomain.get();
	}
	
	public static void reNew() {
		userDomain = new ThreadLocal<VeryCDUserDomain>();
		userDomain.set(new VeryCDUserDomain());
	}

	public static void setInstance(VeryCDUserDomain domain) {
		userDomain.set(domain);
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
