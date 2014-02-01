/**
 * 
 */
package org.helper.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.helper.domain.FarmDomain;
import org.helper.util.FarmKeyGenerator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author lucy
 * 
 */
public class WormFriendService extends BaseService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helper.service.BaseService#extendRequestHeader()
	 */
	@Override
	protected List<BasicHeader> extendRequestHeader() {
		return null;
	}

	public JSONObject wormFriend(String friendId, String fieldId) throws ClientProtocolException, IOException, ParseException,
			org.json.simple.parser.ParseException {
		String time = String.valueOf(System.currentTimeMillis() / 1000);
		StringBuilder url = new StringBuilder("http://happyfarm.manyou-apps.com/api.php?mod=farmlandstatus&act=spraying&farmKey=");
		url.append(FarmKeyGenerator.generatorFarmKey(time)).append("&farmTime=").append(time).append("&inuId=");
		setUrl(url.toString());
		Map<String, String> loginParam = new HashMap<String, String>();
		loginParam.put("place", fieldId);
		loginParam.put("tId", "0");
		loginParam.put("ownerId", friendId);
		setFormParamMap(loginParam);
		HttpResponse response = doPost();
		return (JSONObject) new JSONParser().parse(EntityUtils.toString(response.getEntity()));
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
