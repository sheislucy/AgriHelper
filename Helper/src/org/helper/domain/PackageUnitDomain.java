package org.helper.domain;

import java.io.Serializable;

import org.helper.enums.EmPackageType;

public class PackageUnitDomain implements Serializable {
	private static final long serialVersionUID = 1L;

	private EmPackageType type;
	private String amount;
	private String name;
	private String cId; // 种子id
	private String tId; // 化肥id

	public EmPackageType getType() {
		return type;
	}

	public void setType(EmPackageType type) {
		this.type = type;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String gettId() {
		return tId;
	}

	public void settId(String tId) {
		this.tId = tId;
	}

}
