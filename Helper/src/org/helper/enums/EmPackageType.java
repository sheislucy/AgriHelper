package org.helper.enums;

public enum EmPackageType {
	SEED(1), TOOL(3), DECORATION(2);

	private int code;

	private EmPackageType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
