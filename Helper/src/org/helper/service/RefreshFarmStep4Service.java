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
import org.helper.util.FarmKeyGenerator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class RefreshFarmStep4Service extends BaseService {

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		return null;
	}

	public JSONObject getFarmAndPlayerInfo() throws ClientProtocolException,
			IOException, ParseException {
		String time = String.valueOf(System.currentTimeMillis() / 1000);
		StringBuilder url = new StringBuilder(
				"http://kxnc.manyou.yeswan.com/api.php?mod=user&act=run&farmKey=");
		url.append(FarmKeyGenerator.generatorFarmKey(time))
				.append("&farmTime=").append(time).append("&inuId=");
		setUrl(url.toString());
		HttpResponse response = doGet();
		String responseBody = EntityUtils.toString(response.getEntity());
		return (JSONObject) new JSONParser().parse(responseBody);
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
		BasicClientCookie cookie = new BasicClientCookie(
				uidEntry.getKey(), (String)uidEntry.getValue());
		cookie.setDomain("yeswan.com");
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		return cookieStore;
	}

}
