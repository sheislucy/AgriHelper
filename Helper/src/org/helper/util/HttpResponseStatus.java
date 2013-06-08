package org.helper.util;

public enum HttpResponseStatus {
	SUCCESS("success"), ERROR("error"), TIME_OUT("time_out");
	private String value;

	private HttpResponseStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
