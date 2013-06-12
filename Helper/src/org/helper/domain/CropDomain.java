package org.helper.domain;

import java.io.Serializable;

public class CropDomain implements Serializable {
	private static final long serialVersionUID = 9157897291931099291L;
	private String cId;
	private String cName;
	private String cType;
	private String growthCycle;
	private String maturingTime;
	private String expect;
	private String output;
	private String sale;
	private String price;
	private String FBPrice;
	private String cLevel;
	private String cropExp;
	private String cCharm;
	private String cropChr;
	private String reMaturingTime;

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public String getcType() {
		return cType;
	}

	public void setcType(String cType) {
		this.cType = cType;
	}

	public String getGrowthCycle() {
		return growthCycle;
	}

	public void setGrowthCycle(String growthCycle) {
		this.growthCycle = growthCycle;
	}

	public String getMaturingTime() {
		return maturingTime;
	}

	public void setMaturingTime(String maturingTime) {
		this.maturingTime = maturingTime;
	}

	public String getExpect() {
		return expect;
	}

	public void setExpect(String expect) {
		this.expect = expect;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getSale() {
		return sale;
	}

	public void setSale(String sale) {
		this.sale = sale;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getFBPrice() {
		return FBPrice;
	}

	public void setFBPrice(String fBPrice) {
		FBPrice = fBPrice;
	}

	public String getcLevel() {
		return cLevel;
	}

	public void setcLevel(String cLevel) {
		this.cLevel = cLevel;
	}

	public String getCropExp() {
		return cropExp;
	}

	public void setCropExp(String cropExp) {
		this.cropExp = cropExp;
	}

	public String getcCharm() {
		return cCharm;
	}

	public void setcCharm(String cCharm) {
		this.cCharm = cCharm;
	}

	public String getCropChr() {
		return cropChr;
	}

	public void setCropChr(String cropChr) {
		this.cropChr = cropChr;
	}

	public String getReMaturingTime() {
		return reMaturingTime;
	}

	public void setReMaturingTime(String reMaturingTime) {
		this.reMaturingTime = reMaturingTime;
	}

}
