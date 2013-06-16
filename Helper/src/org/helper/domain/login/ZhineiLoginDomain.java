package org.helper.domain.login;

import java.io.Serializable;

public class ZhineiLoginDomain implements Serializable {
	private static final long serialVersionUID = -1181577654820955575L;
	private String loginUrl;
	private String loginType;
	private String formHash;
	private String loginSubmit;

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
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

}
