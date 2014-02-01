/**
 * 
 */
package org.helper.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lucy
 * 
 */
public class BaseFarmDomain implements Serializable {
	private static final long serialVersionUID = -3627428284561126709L;
	private String userId;
	private String userName;
	private String money;
	private String exp;
	private List<FieldUnitDomain> fieldList = new ArrayList<FieldUnitDomain>();

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public List<FieldUnitDomain> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<FieldUnitDomain> fieldList) {
		this.fieldList = fieldList;
	}

	public void removeAllFields() {
		getFieldList().clear();
	}

	public void addField(FieldUnitDomain unit) {
		getFieldList().add(unit);
	}
}