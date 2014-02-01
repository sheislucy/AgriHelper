/**
 * 
 */
package org.helper.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.helper.domain.BaseFarmDomain;
import org.helper.domain.FarmDomain;
import org.helper.util.FarmKeyGenerator;
import org.helper.util.HelperLoggerAppender;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author lucy
 * 
 */
public class RefreshFriendService extends BaseService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helper.service.BaseService#extendRequestHeader()
	 */
	@Override
	protected List<BasicHeader> extendRequestHeader() {
		return null;
	}

	public void refreshFriend() throws ParseException, IOException, org.json.simple.parser.ParseException {
		String time = String.valueOf(System.currentTimeMillis() / 1000);
		StringBuilder url = new StringBuilder("http://happyfarm.manyou-apps.com/api.php?mod=friend&farmKey=");
		url.append(FarmKeyGenerator.generatorFarmKey(time)).append("&farmTime=").append(time).append("&inuId=");
		setUrl(url.toString());
		Map<String, String> params = new HashMap<String, String>();
		params.put("fv", "0");
		params.put("refresh", "true");
		setFormParamMap(params);
		HttpResponse response = doPost();
		Object json = new JSONParser().parse(EntityUtils.toString(response.getEntity()));
		if (json instanceof JSONArray) {
			buildFriends((JSONArray) json);
		} else {
			HelperLoggerAppender.writeLog("刷新好友失败：" + ((JSONObject) json).get("error"));
		}
	}

	private void buildFriends(JSONArray friendArray) {
		FarmDomain.getInstance().removeAllFriends();
		for (Object jsonUnit : friendArray) {
			String userId = String.valueOf(((JSONObject) jsonUnit).get("userId"));
			if (!userId.equals(FarmDomain.getInstance().getUserId())) {
				BaseFarmDomain friend = new BaseFarmDomain();
				friend.setUserId(userId);
				friend.setExp(String.valueOf(((JSONObject) jsonUnit).get("exp")));
				friend.setUserName(String.valueOf(((JSONObject) jsonUnit).get("userName")));
				friend.setMoney(String.valueOf(((JSONObject) jsonUnit).get("money")));
				FarmDomain.getInstance().getFriendList().add(friend);
			}
		}
		Collections.sort(FarmDomain.getInstance().getFriendList());
	}

	@Override
	protected CookieStore buildCookieStore() {
		BasicCookieStore cookieStore = new BasicCookieStore();
		StringBuilder cookieValue = new StringBuilder();

		Entry<String, Object> uidEntry = FarmDomain.getInstance().getFirstCookieByReg(".*_uId");
		if (null != uidEntry) {
			cookieValue.append(uidEntry.getKey()).append("=").append(uidEntry.getValue());
		}
		BasicClientCookie cookie = new BasicClientCookie(uidEntry.getKey(), (String) uidEntry.getValue());
		cookie.setDomain("happyfarm.manyou-apps.com");
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		return cookieStore;
	}

}
