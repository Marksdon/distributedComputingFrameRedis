package com.xzc.concurr.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import com.xzc.concurr.pojo.TransmitDetail;
import com.xzc.concurrServer.util.DownLoader;
import com.xzc.concurrServer.util.EmojiFilterUtils;
import com.xzc.concurrServer.util.WeiboHelper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SourceSearcher {

	
	@Test
	public void test(){
		String id = "4021790348672058";
		TransmitDetail transmitDetails = getTransSource(id);
		System.out.println(transmitDetails);
	}
	
	
	public static TransmitDetail getTransSource(String id) {
		String html = "";
		String url = "https://api.weibo.com/2/statuses/show_batch.json?"
				+ "source=211160679&ids=%s";
		int j = 0;

		while (true) {

			String uurl = String.format(url, id);
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
			html = DownLoader.doGet(next,"","",false);
			if(html != null || (j == 3))
				break;
			j ++;
		}
		/**
		 *be careful the errmsg JSON,so it must tag html.length() > 250 to ensure html is right
		 */
		if (!html.equals("") && html.length() > 250) {
			List<TransmitDetail> list = new ArrayList<TransmitDetail>();


			JSONObject jsonObject = JSONObject.fromObject(html);
			JSONArray statuses = jsonObject.getJSONArray("statuses");
			JSONObject jsonObject2 = statuses.getJSONObject(0);
			/**
			 * 这里如果字段不匹配，就会发生堵塞
			 */
			//			return jsonObject.getInt("reposts_count");

			TransmitDetail transmitdetail = new TransmitDetail();
			try {
				try {


					transmitdetail.setTransmits(jsonObject2.getInt("reposts_count"));
					///get current weibo comment count
					transmitdetail.setComments(jsonObject2.getInt("comments_count"));
				} catch (Exception e) {
					e.printStackTrace();
				}

				//get source blogId
				try {
					transmitdetail.setAnalysisBlogId(jsonObject2.getString("idstr"));
					//					transmitdetail.setAnalysisId(jsonObject2.getString("idstr"));
					transmitdetail.setBlogId(jsonObject2.getString("idstr"));
				} catch (Exception e) {
					System.out.println(html);
					e.printStackTrace();
				}
				//get current blongId

				//get levelId
				//				transmitdetail.setLevelID(0);
				transmitdetail.setLevelId(0);

				//get ori uid
				transmitdetail.setFromUid(null);

				//get current weibo text
				try {
					String contentStr = jsonObject2.getString("text");
					//cut length
					if (contentStr.length() > 600) {
						contentStr = contentStr.substring(0, 500);
					}
					//replace emotion
					contentStr = EmojiFilterUtils.filterEmoji(contentStr);
					transmitdetail.setContent(contentStr);
				} catch (Exception e) {
					System.out.println(html);
					e.printStackTrace();
					transmitdetail.setContent("");
				}

				//get current weibo create time
				String timeStr = jsonObject2.getString("created_at");
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy",Locale.ENGLISH);
				Date date = null;
				try {
					date = sdf.parse(timeStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String finalTime = sdf1.format(date);
				transmitdetail.setTime(finalTime);

				String user=jsonObject2.getString("user");

				if (jsonObject2.containsKey("user")) {

					JSONObject userJson = JSONObject.fromObject(user);

					//get user's id
					transmitdetail.setUid(userJson.getString("idstr"));
					//get user's name
					transmitdetail.setName(userJson.getString("screen_name"));
					//get user's url of head
					transmitdetail.setHeadUrl(userJson.getString("profile_image_url"));

					String[] tempStringArray = userJson.getString("location").split(" ");
					//get user's province
					transmitdetail.setProvince(tempStringArray[0]);
					//get user's city
					if (tempStringArray.length > 1) {
						transmitdetail.setCity(tempStringArray[1]);
					} else {
						transmitdetail.setCity("其他");
					}

					//get uers's post count
					transmitdetail.setPosts(userJson.getInt("statuses_count"));
					//get uers's fans count
					transmitdetail.setFans(userJson.getInt("followers_count"));
				}

				//get current weibo url
				String mid = WeiboHelper.Id2Mid(transmitdetail.getBlogId());
				String uid = transmitdetail.getUid();
				String url1 = "http://weibo.com/" + uid + "/" + mid;
				transmitdetail.setUrl(url1);

				list.add(transmitdetail);

			} catch (Exception e) {
				System.out.println("wrong");
				System.out.println(html);
				e.printStackTrace();
				return null;
			}
			return transmitdetail;
		} else {
			return null;
		}
	}

}
