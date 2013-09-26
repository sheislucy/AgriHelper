package org.helper.util;

public enum EmUrlDomain {
	ZHINEI("职内网登录网关(my.zhinei.com/index.php)"), VERYCD(
			"VeryCD登录网关(secure.verycd.com/signin)"), LIANPEN(
			"脸谱网关(www.lianpunet.com/index.php)");
	private String value;

	private EmUrlDomain(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EmUrlDomain[] getDomains() {
		return EmUrlDomain.values();
	}
}
