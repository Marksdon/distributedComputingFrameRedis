package com.xzc.concurrServer.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class DownLoader {
	public static String doGet(String url, String queryString, String charset,
			boolean pretty) {
		try {

			CloseableHttpClient httpclient = HttpClients.createDefault();

			HttpGet httpget = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
			httpget.setConfig(requestConfig);
			HttpResponse response = httpclient.execute(httpget);

			HttpEntity entity = response.getEntity();

			String html = EntityUtils.toString(entity);

			httpclient.close();
			return html;
		} catch (IOException e) {
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	public static String doPost(String url, Map<String, String> map) {
		String html = "";
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost
		HttpPost httppost = new HttpPost(url);

		// 创建参数队列
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> e : map.entrySet()) {
			formparams.add(new BasicNameValuePair(e.getKey(), e.getValue()));
		}
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			RequestConfig defaultRequestConfig = RequestConfig.custom()
					.setSocketTimeout(60000)
					.setConnectTimeout(60000)
					.setConnectionRequestTimeout(60000)
					.setStaleConnectionCheckEnabled(true)
					.build();
			/*RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();*/
			httppost.setConfig(defaultRequestConfig);
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					html = EntityUtils.toString(entity);
				}
			} finally {

				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return html;
	}

}
