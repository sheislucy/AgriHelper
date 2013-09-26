package org.helper.domain.login;

import java.io.Serializable;

import org.helper.util.EmUrlDomain;

public class BaseStatefulDomain implements Serializable {
	private static final long serialVersionUID = -3457043423619390415L;
	private EmUrlDomain urlDomain;

	public EmUrlDomain getUrlDomain() {
		return urlDomain;
	}

	public void setUrlDomain(EmUrlDomain urlDomain) {
		this.urlDomain = urlDomain;
	}

	public boolean isVeryCD() {
		return this.urlDomain == EmUrlDomain.VERYCD;
	}

	public boolean isZhinei() {
		return this.urlDomain == EmUrlDomain.ZHINEI;
	}

	public boolean isLianpen() {
		return this.urlDomain == EmUrlDomain.LIANPEN;
	}
}
