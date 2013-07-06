package org.helper.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
import org.helper.util.HelperLoggerAppender;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RefreshShopService extends BaseService {

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		return null;
	}

	public JSONObject refreshShopInfo() throws ClientProtocolException,
			IOException, ParseException {
		String time = String.valueOf(System.currentTimeMillis() / 1000);
		StringBuilder url = new StringBuilder(
				"http://kxnc.manyou.yeswan.com/api.php?mod=shop&act=getShopInfo&type=1&farmKey=");
		url.append(FarmKeyGenerator.generatorFarmKey(time))
				.append("&farmTime=").append(time).append("&inuId=");
		setUrl(url.toString());
		HttpResponse response = doGet();
		String responseBody = EntityUtils.toString(response.getEntity());
		updateShopJsonFile(responseBody);
		return (JSONObject) new JSONParser().parse(responseBody);
	}

	private void updateShopJsonFile(String responseBody) {
		File shopFile = new File("shop.json");
		try {
			OutputStreamWriter output = new OutputStreamWriter(
					new FileOutputStream(shopFile));
			output.write(responseBody);
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
			HelperLoggerAppender.writeLog(e.getMessage());
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
		cookie.setDomain("yeswan.com");
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		return cookieStore;
	}
}
