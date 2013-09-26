package org.helper.domain;

import java.io.Serializable;

public class UserPreferenceUnit implements Serializable {
	private static final long serialVersionUID = 6977197268638519663L;
	private String userId;
	private String userName;
	private String password;
	private int domainIndex;

	private boolean isWater = false;
	private boolean isWorm = false;
	private boolean isWeed = false;
	private boolean isHarvest = false;
	private boolean isPlow = false;
	private boolean isBuy = false;
	private boolean isPlant = false;
	private int seedComboIndex;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isWater() {
		return isWater;
	}

	public void setWater(boolean isWater) {
		this.isWater = isWater;
	}

	public boolean isWorm() {
		return isWorm;
	}

	public void setWorm(boolean isWorm) {
		this.isWorm = isWorm;
	}

	public boolean isWeed() {
		return isWeed;
	}

	public void setWeed(boolean isWeed) {
		this.isWeed = isWeed;
	}

	public boolean isHarvest() {
		return isHarvest;
	}

	public void setHarvest(boolean isHarvest) {
		this.isHarvest = isHarvest;
	}

	public boolean isPlow() {
		return isPlow;
	}

	public void setPlow(boolean isPlow) {
		this.isPlow = isPlow;
	}

	public boolean isBuy() {
		return isBuy;
	}

	public void setBuy(boolean isBuy) {
		this.isBuy = isBuy;
	}

	public boolean isPlant() {
		return isPlant;
	}

	public void setPlant(boolean isPlant) {
		this.isPlant = isPlant;
	}

	public int getSeedComboIndex() {
		return seedComboIndex;
	}

	public void setSeedComboIndex(int seedComboIndex) {
		this.seedComboIndex = seedComboIndex;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getDomainIndex() {
		return domainIndex;
	}

	public void setDomainIndex(int domainIndex) {
		this.domainIndex = domainIndex;
	}

}
