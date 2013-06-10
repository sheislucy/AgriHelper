package org.helper.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FarmDomain implements Serializable {
	private static final long serialVersionUID = 1170704177667181436L;
	private Map<String, Object> cookieMap = new HashMap<String, Object>();
	private String userId;
	private String userName;
	private String money;
	private String exp;
	private String charm;
	private String serverTime;
	private List<FieldUnitDomain> fieldList = new ArrayList<FieldUnitDomain>();

	private static ThreadLocal<FarmDomain> farmDomain;

	private FarmDomain() {
	}

	public static FarmDomain getInstance() {
		if (null == farmDomain || null == farmDomain.get()) {
			farmDomain = new ThreadLocal<FarmDomain>();
			farmDomain.set(new FarmDomain());
		}
		return farmDomain.get();
	}

	public void removeAllFields() {
		fieldList.clear();
	}

	public static void setInstance(FarmDomain domain) {
		farmDomain.set(domain);
	}

	public void addCookie(String key, Object value) {
		cookieMap.put(key, value);
	}

	public void addCookies(Map<String, Object> cookies) {
		cookieMap.putAll(cookies);
	}

	public void setCookies(Map<String, Object> cookies) {
		cookieMap = cookies;
	}

	public Object getCookieValue(String cookieName) {
		return cookieMap.get(cookieName);
	}

	/**
	 * Get the first matches value object by the given regular expression.
	 * 
	 * @param regExp
	 * @return null if mo match found
	 */
	public Object getFirstCookieValueByReg(String regExp) {
		Iterator<Entry<String, Object>> it = cookieMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = (Entry<String, Object>) it.next();
			if (entry.getKey().matches(regExp)) {
				return entry.getValue();
			}
		}
		return null;
	}

	public String getFirstCookieKeyByReg(String regExp) {
		Iterator<Entry<String, Object>> it = cookieMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = (Entry<String, Object>) it.next();
			if (entry.getKey().matches(regExp)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public Entry<String, Object> getFirstCookieByReg(String regExp) {
		Iterator<Entry<String, Object>> it = cookieMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = (Entry<String, Object>) it.next();
			if (entry.getKey().matches(regExp)) {
				return entry;
			}
		}
		return null;
	}

	public Map<String, Object> getCookieMap() {
		return cookieMap;
	}

	public void setCookieMap(Map<String, Object> cookieMap) {
		this.cookieMap = cookieMap;
	}

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

	public String getCharm() {
		return charm;
	}

	public void setCharm(String charm) {
		this.charm = charm;
	}

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	public List<FieldUnitDomain> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<FieldUnitDomain> fieldList) {
		this.fieldList = fieldList;
	}

}
