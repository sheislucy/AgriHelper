package org.helper.domain.login;

import java.io.Serializable;

public class LianpuLoginDomain implements Serializable {
	private static final long serialVersionUID = -6451694501778665716L;
	private String loginUrl;
	private String formHash;
	private String loginSubmit;
	private String cookieTime;
	private String refer;

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getFormHash() {
		return formHash;
	}

	public void setFormHash(String formHash) {
		this.formHash = formHash;
	}

	public String getLoginSubmit() {
		return loginSubmit;
	}

	public void setLoginSubmit(String loginSubmit) {
		this.loginSubmit = loginSubmit;
	}

	public String getCookieTime() {
		return cookieTime;
	}

	public void setCookieTime(String cookieTime) {
		this.cookieTime = cookieTime;
	}

	public String getRefer() {
		return refer;
	}

	public void setRefer(String refer) {
		this.refer = refer;
	}

}
