package org.helper.util;

import java.util.HashMap;
import java.util.Map;

public enum EmCookieKeys {
	ZHINEI_AUTH("zhinei_auth"), ZHINEI_LOGINUSER("zhinei_loginuser");

	private String value;

	public static Map<String, EmCookieKeys> MAP = new HashMap<String, EmCookieKeys>();

	static {
		for (EmCookieKeys em : EmCookieKeys.values()) {
			MAP.put(em.getValue(), em);
		}
	}

	private EmCookieKeys(String value) {
		this.value = value;
	}

	public static boolean contains(String key) {
		return MAP.containsKey(key);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
