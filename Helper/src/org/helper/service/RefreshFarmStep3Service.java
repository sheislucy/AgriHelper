package org.helper.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicHeader;
import org.helper.util.CookieSplitter;

public class RefreshFarmStep3Service extends BaseService {

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		return null;
	}

	public void step3GetMethod(String url) throws ClientProtocolException,
			IOException {
		setUrl(url);
		HttpResponse response = doGet();
		CookieSplitter.splitFarm(response.getHeaders("Set-Cookie"));
	}

}
