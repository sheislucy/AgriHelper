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
import org.helper.domain.PackageUnitDomain;
import org.helper.enums.EmPackageType;
import org.helper.util.FarmKeyGenerator;
import org.helper.util.HelperConstants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PackageService extends BaseService {

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		return null;
	}

	public void refreshPackageInfo() throws ClientProtocolException,
			IOException, ParseException {
		FarmDomain.getInstance().removeAllPackage();
		String time = String.valueOf(System.currentTimeMillis() / 1000);
		StringBuilder url = new StringBuilder(
				"http://happyfarm.manyou-apps.com/api.php?mod=Package&act=getPackageInfo&farmKey=");
		url.append(FarmKeyGenerator.generatorFarmKey(time))
				.append("&farmTime=").append(time).append("&inuId=");
		setUrl(url.toString());
		HttpResponse response = doGet();
		String responseBody = EntityUtils.toString(response.getEntity(),
				HelperConstants.ENCODING_UTF8);
		buildPackage((JSONObject) new JSONParser().parse(responseBody));
	}

	private void buildPackage(JSONObject packageJson) {
		JSONArray seedArray = (JSONArray) packageJson.get("1");
		if (null != seedArray) {
			for (Object seed : seedArray) {
				PackageUnitDomain seedDomain = new PackageUnitDomain();
				seedDomain.setType(EmPackageType.SEED);
				seedDomain.setAmount(String.valueOf(((JSONObject) seed)
						.get("amount")));
				seedDomain
						.setcId(String.valueOf(((JSONObject) seed).get("cId")));
				seedDomain.setName(String.valueOf(((JSONObject) seed)
						.get("cName")));
				FarmDomain.getInstance().addPackage(seedDomain);
			}
		}
		JSONArray toolArray = (JSONArray) packageJson.get("3");
		if (null != toolArray) {
			for (Object tool : toolArray) {
				PackageUnitDomain toolDomain = new PackageUnitDomain();
				toolDomain.setType(EmPackageType.TOOL);
				toolDomain.setAmount(String.valueOf(((JSONObject) tool)
						.get("amount")));
				toolDomain
						.settId(String.valueOf(((JSONObject) tool).get("tId")));
				toolDomain.setName(String.valueOf(((JSONObject) tool)
						.get("tName")));
				FarmDomain.getInstance().addPackage(toolDomain);
			}
		}
	}

	@Override
	protected CookieStore buildCookieStore() {
		BasicCookieStore cookieStore = new BasicCookieStore();
		StringBuilder cookieValue = new StringBuilder();

		Entry<String, Object> uidEntry = FarmDomain.getInstance()
				.getFirstCookieByReg(".*_uId");
		if (null != uidEntry) {
			cookieValue.append(uidEntry.getKey()).append("=")
					.append(uidEntry.getValue());
		}
		BasicClientCookie cookie = new BasicClientCookie(uidEntry.getKey(),
				(String) uidEntry.getValue());
		cookie.setDomain("happyfarm.manyou-apps.com");
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		return cookieStore;
	}

}
