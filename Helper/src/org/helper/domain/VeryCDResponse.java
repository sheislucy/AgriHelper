package org.helper.domain;

import java.io.Serializable;

public class VeryCDResponse implements Serializable {
	private static final long serialVersionUID = -2273786623710283172L;
	private String status;
	private String msg;
	private String info;

	public VeryCDResponse() {
	}

	public VeryCDResponse(String status) {
		this.status = status;
	}

	public VeryCDResponse(String status, String msg, String info) {
		this.status = status;
		this.msg = msg;
		this.info = info;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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
