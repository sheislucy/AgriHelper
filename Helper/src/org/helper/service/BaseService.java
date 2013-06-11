package org.helper.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;

public abstract class BaseService {

	protected DefaultHttpClient httpClient;

	protected PoolingClientConnectionManager clientManager;

	protected HttpHost proxy;

	protected SchemeRegistry schemeRegistry;

	private String url;

	private Map<String, String> formParamMap;

	private void init() {
		schemeRegistry = new org.apache.http.conn.scheme.SchemeRegistry();
//		proxy = new HttpHost("127.0.0.1", 8888);
	}

	protected HttpResponse doPost() throws ClientProtocolException, IOException {
		init();
		HttpPost postRequest = new HttpPost(getUrl());
		List<BasicHeader> headers = buildCommonHeaders();
		headers.addAll(extendRequestHeader());

		BasicHttpParams params = new BasicHttpParams();
		params.setParameter(ClientPNames.DEFAULT_HEADERS, headers);

		buildRegistry();

		clientManager = new PoolingClientConnectionManager(schemeRegistry);
		httpClient = new DefaultHttpClient(clientManager, params);
//		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
//				proxy);

		HttpEntity entity = new UrlEncodedFormEntity(buildFormParameters(),
				"UTF-8");
		postRequest.setEntity(entity);

		httpClient.setCookieStore(buildCookieStore());
		return httpClient.execute(postRequest);
	}

	protected HttpResponse doGet() throws ClientProtocolException, IOException {
		init();
		HttpGet getRequest = new HttpGet(getUrl());
		List<BasicHeader> headers = buildCommonHeaders();
		if (null != extendRequestHeader()) {
			headers.addAll(extendRequestHeader());
		}

		BasicHttpParams params = new BasicHttpParams();
		params.setParameter(ClientPNames.DEFAULT_HEADERS, headers);

		buildRegistry();

		clientManager = new PoolingClientConnectionManager(schemeRegistry);
		httpClient = new DefaultHttpClient(clientManager, params);
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
				proxy);

		httpClient.setCookieStore(buildCookieStore());
		return httpClient.execute(getRequest);
	}

	protected CookieStore buildCookieStore() {
		return new BasicCookieStore();
	}

	protected abstract List<BasicHeader> extendRequestHeader();

	protected List<NameValuePair> buildFormParameters() {
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		Iterator<Entry<String, String>> it = getFormParamMap().entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = (Entry<String, String>) it.next();
			formParams.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		return formParams;
	}

	protected void buildRegistry() {
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
				.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory
				.getSocketFactory()));
	}

	protected List<BasicHeader> buildCommonHeaders() {
		List<BasicHeader> headers = new ArrayList<BasicHeader>();
		// headers.add(new BasicHeader("Host", "www.verycd.com"));
		// headers.add(new BasicHeader("Origin", "http://www.verycd.com"));
		// headers.add(new BasicHeader("Referer", "http://www.verycd.com/"));
		headers.add(new BasicHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8"));
		return headers;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getFormParamMap() {
		return formParamMap;
	}

	public void setFormParamMap(Map<String, String> formParamMap) {
		this.formParamMap = formParamMap;
	}
}
