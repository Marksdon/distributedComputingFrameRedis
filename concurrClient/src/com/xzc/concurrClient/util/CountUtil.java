package com.xzc.concurrClient.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xzc.concurr.pojo.BaseAnalysis;
import com.xzc.concurr.pojo.Result;
import com.xzc.concurr.pojo.TransmitDetail;

import net.sf.json.JSONObject;

public class CountUtil {
	
	private CountUtil() {
	}
	
	/**
	 * 传进来一个定义的map，按日期计算转发数和评论数
	 * @param text 从text中找匹配字符
	 * @param date 传进来的日期
	 * @param map 传进来的map，用于累加已经有的计算结果
	 * @return 返回一个已经计算好的评论转发map
	 */
	public static Map<String, Map<String, Integer>> parseTranCom(String text, String date, 
			Map<String, Map<String, Integer>> map){

		//用来计算的,计算完成后就转化为对象
		//           date        t/m      count
		//		Map<String, Map<String, Integer>> dupMap = new HashMap<>();

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
	 * 提取表情，并且计算想同表情的数目
	 * @param text 传进来的字符串，从中提取表情
	 * @param map 传进方法内的map，用于计算表情数据
	 * @return 返回已经计算累加的map
	 */
	public static Map<String, Integer> parseExpression(String text, 
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
	 * 计算关键词
	 * @param text 传入方法的字符串，要先经过滤
	 * @param map 传进入方法的map，用于累计计算关键字数目
	 * @return 返回统计后的关键字结果map
	 */
	public static Map<String, Integer> parseKeyword(String text, Map<String, Integer> map) {

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
	
	
	/**
	 * 用户系列的计算统计，包括transmitDetail 对象的基本设置
	 * 地区计算，用户类型，粉丝，水军 这些数据都是累积量
	 * @param user 解析的用户数据
	 */
	public static Result countUserSerial(String user, Result result) {

		JSONObject userJson = JSONObject.fromObject(user);
		//下面的都是累积量，需要从原来数据取出
		BaseAnalysis ba = result.getBaseAnalysis();
		Map<String, Integer> areaMap = ba.getAreaMap();
		Map<String, Integer> userTypeMap = ba.getUserTypeMap();
		Map<String, Integer> fansMap = ba.getUserFansMap();
		int shuiJunCount = ba.getShuiJunCount();
		int[] rang = new int[10]; 

		//基本设值
		TransmitDetail td  = setTransmitDetailOnUser(userJson);
		//计算地区
		areaMap = parseArea(userJson, areaMap);
		//计算用户类型	
		userTypeMap = parseUerType(userJson, userTypeMap);
		//计算粉丝
		rang = parseFans(userJson, rang);
		//计算水军
		shuiJunCount = parseShuiJun(userJson, shuiJunCount);
		
		ba.setAreaMap(areaMap);
		ba.setUserTypeMap(userTypeMap);
		for (int i = 0; i < rang.length; i++) {
			fansMap.put("rang"+i, rang[i]);
		}
		ba.setUserFansMap(fansMap);
		ba.setShuiJunCount(shuiJunCount);
		result.getTransmitDetails().add(td);
		result.setBaseAnalysis(ba);
		return result;
	}
	
	
	/**
	 * 计算用户的地区数目
	 * @param userJson 传入发json对象
	 * @param map 传入的map,用于累加计算地区的数目
	 * @return 返回计算完的地区map
	 */
	public static Map<String, Integer> parseArea(JSONObject userJson, Map<String, Integer> map) {
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
	public static Map<String, Integer> parseUerType(JSONObject userJson, 
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
	public static  int[] parseFans(JSONObject userJson, int[] rang) {

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
	public static int parseShuiJun(JSONObject userJson , int shuiJunCount) {
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
	 * 传进来json数据，解析并封装到TransmitDetail对象中
	 * @param userJson 需要解析封装json数据
	 * @return 封装好的TransmitDetail对象
	 */
	public static TransmitDetail setTransmitDetailOnUser(JSONObject userJson) {
		
		TransmitDetail transmitdetail = new TransmitDetail();
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
