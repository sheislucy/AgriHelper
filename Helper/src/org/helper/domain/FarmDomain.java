package org.helper.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.helper.util.StringUtils;

public class FarmDomain extends BaseFarmDomain {
	private static final long serialVersionUID = 1170704177667181436L;
	private Map<String, Object> cookieMap = new HashMap<String, Object>();
	private String charm;
	private String serverTime;
	private List<StoreUnitDomain> storeList = new ArrayList<StoreUnitDomain>();
	private List<PackageUnitDomain> packageList = new ArrayList<PackageUnitDomain>();
	private List<BaseFarmDomain> friendList = new ArrayList<>();
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

	public static void reNew() {
		farmDomain = new ThreadLocal<FarmDomain>();
		farmDomain.set(new FarmDomain());
	}

	public FieldUnitDomain getFieldUnitDomainById(String id) {
		int fid = Integer.parseInt(id);
		for (FieldUnitDomain d : getFieldList()) {
			if (Integer.parseInt(d.getA()) == fid) {
				return d;
			}
		}
		return null;
	}

	public StoreUnitDomain getStoreUnitDomainByIndex(int index) {
		if (index <= storeList.size()) {
			return storeList.get(index);
		}
		return null;
	}

	public BaseFarmDomain getFriendById(String userId) {
		for (BaseFarmDomain friend : friendList) {
			if (friend.getUserId().equals(userId)) {
				return friend;
			}
		}
		return null;
	}

	public boolean hasSeed(String seedId) {
		for (PackageUnitDomain unit : packageList) {
			if (unit.getcId() != null && unit.getcId().equalsIgnoreCase(seedId) && StringUtils.isEmpty(unit.getAmount())
					&& !unit.getAmount().equals("0")) {
				return true;
			}
		}
		return false;
	}

	public void removeOneSeed(String seedId) {
		for (PackageUnitDomain unit : packageList) {
			if (unit.getcId() != null && unit.getcId().equalsIgnoreCase(seedId)) {
				int amount = Integer.parseInt(unit.getAmount()) - 1;
				unit.setAmount(String.valueOf(amount + ""));
				break;
			}
		}
	}

	public void removeAllStorage() {
		storeList.clear();
	}

	public void removeAllPackage() {
		packageList.clear();
	}

	public void removeAllFriends() {
		friendList.clear();
	}

	public void addStore(StoreUnitDomain unit) {
		storeList.add(unit);
	}

	public void addPackage(PackageUnitDomain unit) {
		packageList.add(unit);
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

	public List<StoreUnitDomain> getStoreList() {
		return storeList;
	}

	public void setStoreList(List<StoreUnitDomain> storeList) {
		this.storeList = storeList;
	}

	public List<PackageUnitDomain> getPackageList() {
		return packageList;
	}

	public void setPackageList(List<PackageUnitDomain> packageList) {
		this.packageList = packageList;
	}

	public List<BaseFarmDomain> getFriendList() {
		return friendList;
	}

	public void setFriendList(List<BaseFarmDomain> friendList) {
		this.friendList = friendList;
	}

}
