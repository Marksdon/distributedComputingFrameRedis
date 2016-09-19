
package com.xzc.concurrServer.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.xzc.concurr.pojo.Blogger;
import com.xzc.concurr.pojo.TransmitDetail;

public class TestUtil {

	/**
	 * 遍历blogger的list
	 * @param list
	 */
	public static void travalList(List<Blogger> list) {
		for (int i = 0; i < list.size(); i++) {
			Blogger blogger = list.get(i);
			System.out.println(blogger.getBlogId() + "|" 
					+ blogger.getUid() + "|" + blogger.getTransmits());
		}

	}

	/**
	 * 模拟计算任务 
	 * @param blogger blogger对象
	 * @return 计算处理后的blogger对象
	 */
	public static Blogger countBlogger(Blogger blogger) {
		blogger.setBlogId("mission completed");
		blogger.setUid("Congra");
		blogger.setTransmits(0);
		return blogger;
	}


	@Test
	/**
	 * 测试请求数据
	 * @return 返回单页请求回来的数据
	 */
	public void testRequestData() {
		String blogId = "3976483724070984";
		String url = "https://api.weibo.com/2/statuses/repost_timeline.json?"
				+ "access_token=2.00RNvdXD06XASOa821bf538fAoTWBB&id=%s&page=%s";
		String uurl = String.format(url, blogId, "1");
		String json = DownLoader.doGet(uurl,"","",false);
		System.out.println(json);
	}


	/**
	 * 类型,微博分析(1-转发评论数，2-用户类型，3-表情，4-认证类型，5-关键词)
	 * 
	 * 1.基本类型对象--baseAnalysis
	 * 2.转发对象 --transmitDetail
	 * 前面两个对象组合为一个对象 result
	 * 
	 */


	/**
	 * 传进来一个定义的map，按日期计算转发数和评论数
	 * @param text 从text中找匹配字符
	 * @param date 传进来的日期
	 * @param map 传进来的map，用于累加已经有的计算结果
	 * @return 返回一个已经计算好的评论转发map
	 */
	public Map<String, Map<String, Integer>>  parseTranCom(String text, String date, 
			Map<String, Map<String, Integer>> map){

		//用来计算的,计算完成后就转化为对象
		//       date        t/m      count
		//	Map<String, Map<String, Integer>> dupMap = new HashMap<>();

		Pattern p = Pattern.compile("转发微博");
		Matcher m = p.matcher(text);

		//判断是否匹配
		if (m.find()) {
			//判断是否存在该日期
			if (!map.containsKey(date)) {
				//
				Map<String, Integer> subMap = new HashMap<>();
				subMap.put("评论数", 1);
				subMap.put("转发数", 0);
				map.put(date, subMap);
			} else {
				map.get(date).put("评论数", map.get(date).get("评论数") + 1);
			}
		} else {
			if (!map.containsKey(date)) {
				HashMap<String, Integer> subMap2 = new HashMap<>();
				subMap2.put("评论数", 0);
				subMap2.put("转发数", 1);
				map.put(date, subMap2);
			} else {
				map.get(date).put("转发数", map.get(date).get("转发数") + 1);
			}
		}
		return map;
	}


	/**
	 * 评论转发测试
	 * 1.日期
	 * 2.个数
	 */
	@Test
	public void testParseTransCom() {

		List<String> textList = new ArrayList<String>();
		textList.add("转发微博");
		textList.add("转发微博");
		textList.add("微博");
		textList.add("转发微博");
		textList.add("转转微博");
		textList.add("转转微博");
		textList.add("转转微博");
		textList.add("转转微博");
		textList.add("微博");
		textList.add("微博");

		//String text = "转发微博";
		String date = "2016-09-02";
		String date1 = "2016-09-03";

		Map<String, Map<String, Integer>> map = new HashMap<>();

		for(String text: textList) {
			map = parseTranCom(text, date, map);
			System.out.println(map);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for(String text: textList) {
			map = parseTranCom(text, date1, map);
			System.out.println(map);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//		System.out.println("==========");
		//		System.out.println(map);

		String text = "";
		String date2 = "2016-09-06";
		map = parseTranCom(text, date2, map);
		System.out.println(map);

		String text1 = "转发微博";
		String date3 = "2016-09-12";
		map = parseTranCom(text1, date3, map);
		System.out.println(map);

	}


	/**
	 * 提取表情，并且计算想同表情的数目
	 * @param text 传进来的字符串，从中提取表情
	 * @param map 传进方法内的map，用于计算表情数据
	 * @return 返回已经计算累加的map
	 */
	public Map<String, Integer> parseExpression(String text, 
			Map<String, Integer> map) {
		Pattern p = Pattern.compile("\\[.*?\\]");
		Matcher m = p.matcher(text);
		String key = null;

		while(m.find()) {
			key = m.group();
			if (!map.containsKey(key)) {
				map.put(key, 1);
			} else {
				map.put(key, map.get(key)+1);
			}
		}
		return map;
	}

	/**
	 * 测试表情计算方法
	 * 1.测试个数
	 */
	@Test
	public void testParseExpression() {
		Map<String, Integer> map = new HashMap<>();
		List<String> expList = new ArrayList<String>();
		expList.add("好好学习天天向上[拍照],[haha]");
		expList.add("sdl;fksdflk[微笑]sdf");
		expList.add("alsdkfjkjdfgfk[微笑]sdjfsdsfjs;df");
		expList.add("落实到快放假[哈哈][哈哈][哈哈]asdfasdf");

		for(String text: expList){
			map = parseExpression(text, map);
			System.out.println(map);
		}
		System.out.println("=============");
		System.out.println(map);
	}


	/**
	 * 计算关键词
	 * @param text 传入方法的字符串，要先经过滤
	 * @param map 传进入方法的map，用于累计计算关键字数目
	 * @return 返回统计后的关键字结果map
	 */
	public Map<String, Integer> parseKeyword(String text, Map<String, Integer> map) {

		//请求
		//计算
		List<String> strList = new ArrayList<>();
		try {
			//请求分词结果
			text = URLEncoder.encode(text, "utf-8");
			String ppl = String.format("http://60.28.252.121:8015/yuSegmenter?text=%s",text);  
			String word = DownLoader.doGet(ppl, "", "UTF-8", true);

			if (word != null) {
				word = word.replace("(", "").replace(")", "");
				String arrays[] = word.split("\\+");
				List<String> words = new ArrayList<>();
				for(int k = 0; k < arrays.length; k++) {
					words.add(arrays[k].replace("[", "").replace("]", ""));
				}
				strList.addAll(words);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for(String temp: strList) {
			if(temp.length() > 1){
				Integer count = map.get(temp);  
				map.put(temp, (count == null) ? 1 : count + 1);  
			}
		}   
		return map;
	}


	@Test
	public void testParseKeyword(){
		Map<String, Integer> map = new HashMap<>();
		List<String> list = new ArrayList<>();
		list.add("好好学习天天向上[拍照],[haha]");
		list.add("学习天天向上");
		list.add("学习天天向上");
		list.add("sdl;fksdflk[微笑]sdf");
		list.add("alsdkfjkjdfgfk[微笑]sdjfsdsfjs;df");
		list.add("落实到快放假[哈哈][哈哈][哈哈]asdfasdf");
		list.add("hunter");
		list.add("hunting");
		list.add("hunter");

		for(String text: list){
			map = parseKeyword(text,map);
			System.out.println(map);
		}

		System.out.println(map);

	}


	/**
	 *解析时，a tip jsonObject.contianKey()判断是否含有该字段 
	 */

	public void countUserSerial(String user) {

		JSONObject userJson = JSONObject.fromObject(user);
		TransmitDetail transmitdetail = new TransmitDetail();
		Map<String, Integer> areaMap = new HashMap<>();
		Map<String, Integer> userTypeMap = new HashMap<>();
		int[] rang = new int[10]; 
		int shuiJunCount = 0;

		//基本设值
		transmitdetail = setTransmitDetailOnUser(userJson, transmitdetail);
		//计算地区
		areaMap = parseArea(userJson, areaMap);
		//计算用户类型	
		userTypeMap = parseUerType(userJson, userTypeMap);
		//计算粉丝
		rang = parseFans(userJson, rang);
		//计算水军
		shuiJunCount = parseShuiJun(userJson, shuiJunCount);

	}


	/**
	 * 计算用户的地区数目
	 * @param userJson 传入发json对象
	 * @param map 传入的map,用于累加计算地区的数目
	 * @return 返回计算完的地区map
	 */
	public Map<String, Integer> parseArea(JSONObject userJson, Map<String, Integer> map) {
		String[] areaArr = userJson.getString("location").split(" ");
		//计算地区
		if (map.containsKey(areaArr[0])) {
			map.put(areaArr[0], map.get(areaArr[0])+1);
		} else {
			map.put(areaArr[0], 1);
		}
		return map;
	}

	/**
	 *  传入json对象，计算用户类型
	 * @param userJson 传入json对象
	 * @param map 用户类型map，用户累计计算用户类型,在传入之前应该初始化
	 * @return 返回计算完的用户类型map
	 */
	public Map<String, Integer> parseUerType(JSONObject userJson, 
			Map<String, Integer> map) {

		if(userJson.getString("verified_type").equals("-1")) {
			map.put("comTypeCount", map.get("comTypeCount")+1);
		} else if(userJson.getString("verified_type").equals("0")) {
			map.put("cerTypeCount", map.get("cerTypeCount")+1);
		} else if(userJson.getString("verified_type").equals("200")||
				userJson.getString("verified_type").equals("220")){
			map.put("weiboArtTypeCount", map.get("weiboArtTypeCount")+1);
		} else {
			map.put("weiboArtTypeCount", map.get("weiboArtTypeCount")+1);
		}
		return map;
	}


	/**
	 * 传入已经json对象，计算不同量级的粉丝个数
	 * @param userJson 传入的的json对象
	 * @param rang 传入的数据，用于累加计算不同量级的粉丝数目传入之前需要初始化
	 * @return 返回的计算结果
	 */
	public int[] parseFans(JSONObject userJson, int[] rang) {

		//计算粉丝
		int fansCount = userJson.getInt("followers_count");
		if (fansCount >= 0 && fansCount < 50) {
			rang[0] ++;
		} else if (fansCount >= 50 && fansCount < 100) {
			rang[1] ++;
		} else if (fansCount >= 100 && fansCount < 200) {
			rang[2] ++;
		} else if (fansCount >= 200 && fansCount < 500) {
			rang[3] ++;
		} else if (fansCount >= 500 && fansCount < 1000) {
			rang[4] ++;
		} else if (fansCount >= 1000 && fansCount < 10000) {
			rang[5] ++;
		} else if (fansCount >= 10000 && fansCount < 50000) {
			rang[6] ++;
		} else if (fansCount >= 50000 && fansCount < 100000) {
			rang[7] ++;
		} else if (fansCount >= 100000) {
			rang[8] ++;
		}
		return rang;
	}


	/**
	 * 累加计算水军的数目
	 * @param userJson 传入的json对象
	 * @param shuiJunCount 传入的水军数目
	 * @return 返回计算完的水军数目
	 */
	public int parseShuiJun(JSONObject userJson , int shuiJunCount) {
		//计算水军
		if((userJson.getString("verified_type").equals("-1") && 
				userJson.getInt("followers_count") < 50) || 
				(TimeTool.getDays(userJson.getString("created_at"))<90 && 
						userJson.getInt("statuses_count") < 20)) {
			shuiJunCount ++;
		} 
		return shuiJunCount;
	}


	/**
	 * 设置TransmitDetail 对象用户属性
	 * @param userJson 传入的json对象
	 * @param TransmitDetail 传入的TransmitDetail对象
	 * @return 返回已经设置好了的TransmitDetail对象
	 */
	public TransmitDetail setTransmitDetailOnUser(JSONObject userJson, TransmitDetail transmitdetail) {

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
		return transmitdetail;

	}


	/**
	 * 
	 * @param jObj
	 * @param transmitDetail
	 * @param analysisID
	 * @param sourceBlogId
	 * @return
	 */
	public TransmitDetail setTransmitDetail(JSONObject jObj,TransmitDetail transmitDetail,
			String analysisID, String sourceBlogId) {
		transmitDetail.setTransmits(jObj.getInt("reposts_count"));
//		transmitDetail.setAnalysisId(analysisID);
		transmitDetail.setAnalysisBlogId(analysisID);
	
		//get current blongId
		transmitDetail.setBlogId(jObj.getString("idstr"));
		//get levelId
		transmitDetail.setLevelId(1);
		///get current weibo comment count
		transmitDetail.setComments(jObj.getInt("comments_count"));

		//获得源博主的uid
		transmitDetail.setFromUid(sourceBlogId);
		//get current weibo text
		String text = jObj.getString("text"); //这里要过滤一下 
		transmitDetail.setContent(text);
		//get current weibo create time
		String timeStr = jObj.getString("created_at");
		String finalTime = TimeTool.formatTimeOnStamps(timeStr);
		transmitDetail.setTime(finalTime);
		return transmitDetail;
	}
	
	
}
