package org.helper.domain;

import java.io.Serializable;

import org.helper.domain.login.UserDomain;

public class AccountDomain implements Serializable {
	private static final long serialVersionUID = 2186136319361223197L;
	private UserDomain userDomain;
	private FarmDomain farmDomain;
	private boolean autoCareEnable = false;

	public UserDomain getUserDomain() {
		return userDomain;
	}

	public void setUserDomain(UserDomain userDomain) {
		this.userDomain = userDomain;
	}

	public FarmDomain getFarmDomain() {
		return farmDomain;
	}

	public void setFarmDomain(FarmDomain farmDomain) {
		this.farmDomain = farmDomain;
	}

	public boolean isAutoCareEnable() {
		return autoCareEnable;
	}

	public void setAutoCareEnable(boolean autoCareEnable) {
		this.autoCareEnable = autoCareEnable;
	}

}
