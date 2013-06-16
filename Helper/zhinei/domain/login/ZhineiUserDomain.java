package org.helper.domain.login;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ZhineiUserDomain implements Serializable {
	private static final long serialVersionUID = -8608225081404150735L;

	private Map<String, Object> cookieMap = new HashMap<String, Object>();

	private static ThreadLocal<ZhineiUserDomain> userDomain;

	private ZhineiUserDomain() {
	}

	public static ZhineiUserDomain getInstance() {
		if (null == userDomain || null == userDomain.get()) {
			userDomain = new ThreadLocal<ZhineiUserDomain>();
			userDomain.set(new ZhineiUserDomain());
		}
		return userDomain.get();
	}
	
	public static void reNew() {
		userDomain = new ThreadLocal<ZhineiUserDomain>();
		userDomain.set(new ZhineiUserDomain());
	}

	public static void setInstance(ZhineiUserDomain domain) {
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

}
