package org.helper.enums;

import java.util.HashMap;
import java.util.Map;

public enum EmCookieKeys {
	M_GROUP_ID("mgroupId"), DCM("dcm"), EXPIRES("expires"), SID("sid"), MEMBER_ID(
			"member_id"), MEMBER_NAME("member_name"), UCHOME_LOGINUSER(
			"uchome_loginuser"), PASS_HASH("pass_hash"), REMEMBERME(
			"rememberme"), UCHOME_REWARD_LOG("uchome_reward_log"), UCHOME_AUTH(
			"uchome_auth"), DEFAULT(""), ZHINEI_AUTH("zhinei_auth"), ZHINEI_LOGINUSER(
			"zhinei_loginuser");

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

	public static EmCookieKeys convert(String key) {
		return MAP.containsKey(key) ? MAP.get(key) : DEFAULT;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
