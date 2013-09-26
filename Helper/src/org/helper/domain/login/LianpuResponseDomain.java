package org.helper.domain.login;

import java.io.Serializable;

import org.helper.util.HttpResponseStatus;

public class LianpuResponseDomain implements Serializable {
	private static final long serialVersionUID = -8509616412994775332L;
	private HttpResponseStatus status;
	private String infoText;

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

}
