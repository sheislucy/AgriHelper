package org.helper.domain;

import java.io.Serializable;

import org.apache.http.Header;

public class HelperResponse implements Serializable {
	private static final long serialVersionUID = -2273786623710283172L;
	private int status;
	private Header[] headers;
	

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Header[] getHeaders() {
		return headers;
	}

	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}
}
