package org.helper.domain.login;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserDomain extends BaseStatefulDomain implements
		Serializable {
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

	public static void reNew() {
		userDomain = new ThreadLocal<UserDomain>();
		userDomain.set(new UserDomain());
	}

	public static void setInstance(UserDomain domain) {
		userDomain.set(domain);
	}

	public void addCookie(String key, Object value) {
		cookieMap.put(key, value);
	}

	public Object getCookieValue(String cookieName) {
		return cookieMap.get(cookieName);
	}
}
