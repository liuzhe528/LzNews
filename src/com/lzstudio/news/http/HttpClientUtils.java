package com.lzstudio.news.http;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {
	private static HttpClient client = new DefaultHttpClient();
	private static HttpGet httpGet = new HttpGet();

	/**
	 * 联网获取Json数据
	 * 
	 * @param url地址
	 * @return
	 */
	public static String getJson(String url) {
		try {
			httpGet.setURI(URI.create(url));
			HttpResponse response = client.execute(httpGet);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				String result = EntityUtils.toString(response.getEntity(),
						"utf-8");
				if (result != null) {
					return result;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
