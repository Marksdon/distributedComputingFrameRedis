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
	public static Result countUserSerial(String user, Result result, 
			TransmitDetail td) {

		JSONObject userJson = JSONObject.fromObject(user);
		//下面的都是累积量，需要从原来数据取出
		BaseAnalysis ba = result.getBaseAnalysis();
		Map<String, Integer> areaMap = ba.getAreaMap();
		Map<String, Integer> cerTypeMap = ba.getCerTypeMap();

		Map<String, Integer> userFansMap = ba.getUserFansMap();
		Map<String, Integer> shuiJunMap = ba.getShuiJunMap();

		//基本设值
		td = setTransmitDetailOnUser(userJson,td);
		//设置TransmitDetail对象analysisBlogId属性
		td.setAnalysisBlogId(result.getResultId());
		td.setFromUid(result.getResultId());//暂时设定为源微博

		//计算地区
		areaMap = parseArea(userJson, areaMap);
		//计算用户类型	
		cerTypeMap = parseCerType(userJson, cerTypeMap);
		//计算粉丝
		userFansMap = parseFans(userJson, userFansMap);
		//计算水军
		shuiJunMap = parseShuiJun(userJson, shuiJunMap);

		ba.setAreaMap(areaMap);
		ba.setCerTypeMap(cerTypeMap);
		ba.setUserFansMap(userFansMap);
		ba.setShuiJunMap(shuiJunMap);
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
	public static Map<String, Integer> parseArea(JSONObject userJson, 
			Map<String, Integer> map) {
		try {
			String[] areaArr = userJson.getString("location").split(" ");
			//计算地区
			if (map.containsKey(areaArr[0])) {
				map.put(areaArr[0], map.get(areaArr[0])+1);
			} else {
				map.put(areaArr[0], 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}


	/**
	 * 计算用户的认证类型
	 * @param userJson 传入json对象
	 * @param map 用户认证类型累积map
	 * @return 完成一次累积的后的map
	 */
	public static Map<String, Integer> parseCerType(JSONObject userJson, 
			Map<String, Integer> map) {
		try {
			if(userJson.getString("verified_type").equals("-1")) {
				map.put("comType", map.get("comType")+1);
			} else if(userJson.getString("verified_type").equals("0")) {
				map.put("cerType", map.get("cerType")+1);
			} else if(userJson.getString("verified_type").equals("200")||
					userJson.getString("verified_type").equals("220")){
				map.put("weiboArtType", map.get("weiboArtType")+1);
			} else {
				map.put("ofiCerType", map.get("ofiCerType")+1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}


	/**
	 * 计算用户粉丝不同量级的个数
	 * @param userJson 传入的的json对象
	 * @param userFansMap 累积用户粉丝不同量级的map对象
	 * @return 完成一次累积的后的map对象
	 */
	public static Map<String, Integer> parseFans(JSONObject userJson, Map<String, Integer> userFansMap) {

		//计算粉丝
		int fansCount = userJson.getInt("followers_count");
		if (fansCount >= 0 && fansCount < 50) {
			userFansMap.put("rang0", userFansMap.get("rang0")+1);
		} else if (fansCount >= 50 && fansCount < 100) {
			userFansMap.put("rang1", userFansMap.get("rang1")+1);
		} else if (fansCount >= 100 && fansCount < 200) {
			userFansMap.put("rang2", userFansMap.get("rang2")+1);
		} else if (fansCount >= 200 && fansCount < 500) {
			userFansMap.put("rang3", userFansMap.get("rang3")+1);
		} else if (fansCount >= 500 && fansCount < 1000) {
			userFansMap.put("rang4", userFansMap.get("rang4")+1);
		} else if (fansCount >= 1000 && fansCount < 10000) {
			userFansMap.put("rang5", userFansMap.get("rang5")+1);
		} else if (fansCount >= 10000 && fansCount < 50000) {
			userFansMap.put("rang6", userFansMap.get("rang6")+1);
		} else if (fansCount >= 50000 && fansCount < 100000) {
			userFansMap.put("rang7", userFansMap.get("rang7")+1);
		} else if (fansCount >= 100000) {
			userFansMap.put("rang8", userFansMap.get("rang8")+1);
		}
		return userFansMap;
	}


	/**
	 * 累加计算水军用户和真实用户
	 * @param userJson 需要解析的json数据
	 * @param shuiJunMap 水军map累积量
	 * @return 完成累积计算一次的水军map
	 */
	public static Map<String, Integer> parseShuiJun(JSONObject userJson , Map<String, Integer> shuiJunMap) {
		//计算水军用户和真实用户
		if((userJson.getString("verified_type").equals("-1") && 
				userJson.getInt("followers_count") < 50) || 
				(TimeTool.getDays(userJson.getString("created_at"))<90 && 
						userJson.getInt("statuses_count") < 20)) {
			shuiJunMap.put("shuiJun", shuiJunMap.get("shuiJun") + 1);
		} else {
			shuiJunMap.put("realUser", shuiJunMap.get("realUser") + 1);
		}
		return shuiJunMap;
	}


	/**
	 * 传进来json数据，解析并封装到TransmitDetail对象中
	 * @param userJson 需要解析封装json数据
	 * @return 封装好的TransmitDetail对象
	 */
	public static TransmitDetail setTransmitDetailOnUser(JSONObject userJson, 
			TransmitDetail td) {
		td.setUid(userJson.getString("idstr"));
		td.setName(userJson.getString("screen_name"));
		td.setHeadUrl(userJson.getString("profile_image_url"));
		String[] tempStringArray = userJson.getString("location").split(" ");
		td.setProvince(tempStringArray[0]);
		if (tempStringArray.length > 1) {
			td.setCity(tempStringArray[1]);
		} else {
			td.setCity("其他");
		}
		td.setPosts(userJson.getInt("statuses_count"));
		td.setFans(userJson.getInt("followers_count"));
		return td;
	}



	/**
	 * 封装的TransmitDetail的部分属性数据
	 * @param jb 传入的json对象数据
	 * @return 封装数据完成后的TransmitDetail对象
	 */
	public static TransmitDetail setTransmitDetail(JSONObject jb, String text){
		TransmitDetail td = new TransmitDetail();
		td.setBlogId(jb.getString("idstr"));
		td.setLevelId(1);  //层次暂时定位1
		td = setTransmitDetailUrl(td);  //封装url属性数据
		td.setTransmits(jb.getInt("reposts_count"));
		td.setComments(jb.getInt("comments_count"));
		td.setContent(text);
		String time = TimeTool.formatTimeOnStamps(
				jb.getString("created_at"));
		td.setTime(time);
		return td;
	}



	/**
	 * 封装TransmitDetail对象的url属性数据
	 * @param td 需要封装的TransmitDetail对象
	 * @return 完成封装后的TransmitDetail对象
	 */
	private static TransmitDetail setTransmitDetailUrl(TransmitDetail td){
		StringBuilder builder = new StringBuilder();
		String mid = WeiboHelper.Id2Mid(td.getBlogId());
		String uid = td.getUid();
		builder.append("http://weibo.com/").append(uid).append("/").append(mid);
		td.setUrl(builder.toString());
		return td;
	}

}
