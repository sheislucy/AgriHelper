package org.helper.domain.login;

import java.io.Serializable;

import org.helper.enums.HttpResponseStatus;

public class VeryCDResponseDomain implements Serializable {
	private static final long serialVersionUID = -2273786623710283172L;
	private HttpResponseStatus status;
	private String msg;
	private String info;

	public VeryCDResponseDomain() {
	}

	public VeryCDResponseDomain(HttpResponseStatus status) {
		this.status = status;
	}

	public VeryCDResponseDomain(HttpResponseStatus status, String msg, String info) {
		this.status = status;
		this.msg = msg;
		this.info = info;
	}

	public HttpResponseStatus getStatus() {
		return status;
	}

	public void setStatus(HttpResponseStatus status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
