package org.helper.domain.login;

import java.io.Serializable;

import org.helper.util.HttpResponseStatus;

public class ZhineiResponseDomain implements Serializable {
	private static final long serialVersionUID = 2325704733252060821L;
	private HttpResponseStatus status;
	private String infoText;
	private String login3Url;

	public HttpResponseStatus getStatus() {
		return status;
	}

	public void setStatus(HttpResponseStatus status) {
		this.status = status;
	}

	public String getInfoText() {
		return infoText;
	}

	public void setInfoText(String infoText) {
		this.infoText = infoText;
	}

	public String getLogin3Url() {
		return login3Url;
	}

	public void setLogin3Url(String login3Url) {
		this.login3Url = login3Url;
	}

}
