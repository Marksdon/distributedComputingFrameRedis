package com.xzc.concurrClient.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import com.xzc.concurr.pojo.Task;


public class DownLoader {
	
	/**
	 * 工具类不给实例化
	 */
	private DownLoader() {
	}
	
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
	
	
	/**
	 * 获取数据
	 * @param task 传进来任务对象
	 * @return 任务对象需要的数据
	 */
	public static String getData (Task task) {
		
		String taskObjId = (String)task.getObj();
		
		String html = "";
		String url = "https://api.weibo.com/2/statuses/repost_timeline.json?"
				+ "access_token=2.00RNvdXD06XASOa821bf538fAoTWBB&id=%s&page=%s";
		int j = 0;
		while (true) {
			String uurl = String.format(url, taskObjId, task.getiTask());
			String next = url.split("\\?")[0] + "?";
			uurl = uurl.split("\\?")[1];
			String[] ini = uurl.split("&");

			for(int i = 0;i<ini.length;i++) {
				String str = ini[i];
				String[] temp = str.split("=");
				if(temp.length>1) {
					try {
						next+=temp[0]+"="+URLEncoder.encode(temp[1], "utf-8")+"&";
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
			html = doGet(next,"","",false);
			if((html != null &&!html.equals("")) || (j == 7))
				break;
			j ++;
		}
		return html;
	}

}
