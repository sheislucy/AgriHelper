package org.helper.service;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.helper.domain.FarmDomain;
import org.helper.domain.FieldUnitDomain;
import org.helper.util.FarmKeyGenerator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RefreshFarmStep4Service extends BaseService {

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		return null;
	}

	public JSONObject getFarmAndPlayerInfo() throws ClientProtocolException, IOException, ParseException {
		String time = String.valueOf(System.currentTimeMillis() / 1000);
		StringBuilder url = new StringBuilder("http://happyfarm.manyou-apps.com/api.php?mod=user&act=run&farmKey=");
		url.append(FarmKeyGenerator.generatorFarmKey(time)).append("&farmTime=").append(time).append("&inuId=");
		setUrl(url.toString());
		HttpResponse response = doGet();
		String responseBody = EntityUtils.toString(response.getEntity());
		return (JSONObject) new JSONParser().parse(responseBody);
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

	public void refreshFarm() throws ClientProtocolException, IOException, ParseException {
		JSONObject farmJson = getFarmAndPlayerInfo();
		JSONObject user = (JSONObject) farmJson.get("user");
		FarmDomain.getInstance().setCharm(String.valueOf(user.get("charm")));
		FarmDomain.getInstance().setExp(String.valueOf(user.get("exp")));
		// FarmDomain.getInstance().setUserName(
		// String.valueOf(user.get("userName")));
		FarmDomain.getInstance().setUserId(String.valueOf(user.get("uId")));
		FarmDomain.getInstance().setMoney(String.valueOf(user.get("money")));
		JSONArray farmlandStatus = (JSONArray) farmJson.get("farmlandStatus");
		FarmDomain.getInstance().removeAllFields();
		for (Object jsonUnit : farmlandStatus) {
			FieldUnitDomain unit = new FieldUnitDomain();
			unit.setA(String.valueOf(((JSONObject) jsonUnit).get("a")));
			unit.setB(String.valueOf(((JSONObject) jsonUnit).get("b")));
			unit.setF(String.valueOf(((JSONObject) jsonUnit).get("f")));
			unit.setG(String.valueOf(((JSONObject) jsonUnit).get("g")));
			unit.setH(String.valueOf(((JSONObject) jsonUnit).get("h")));
			unit.setJ(String.valueOf(((JSONObject) jsonUnit).get("j")));
			unit.setK(String.valueOf(((JSONObject) jsonUnit).get("k")));
			unit.setL(String.valueOf(((JSONObject) jsonUnit).get("l")));
			unit.setM(String.valueOf(((JSONObject) jsonUnit).get("m")));
			unit.setN(String.valueOf(((JSONObject) jsonUnit).get("n")));
			unit.setQ(String.valueOf(((JSONObject) jsonUnit).get("q")));
			unit.setR(String.valueOf(((JSONObject) jsonUnit).get("r")));
			unit.setS(String.valueOf(((JSONObject) jsonUnit).get("s")));
			unit.setT(String.valueOf(((JSONObject) jsonUnit).get("t")));
			unit.setU(String.valueOf(((JSONObject) jsonUnit).get("u")));
			FarmDomain.getInstance().addField(unit);
		}
	}
}
