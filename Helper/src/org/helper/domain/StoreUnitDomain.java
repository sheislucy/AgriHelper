package org.helper.domain;

import java.io.Serializable;

public class StoreUnitDomain implements Serializable {
	private static final long serialVersionUID = 2017090915702049546L;
	private String cId;
	private String cName;
	private String cType;
	private String amount;
	private String price;

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getcType() {
		return cType;
	}

	public void setcType(String cType) {
		this.cType = cType;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

}
