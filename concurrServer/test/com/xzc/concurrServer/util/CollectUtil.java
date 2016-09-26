package com.xzc.concurrServer.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.xzc.concurr.pojo.BaseAnalysis;
import com.xzc.concurr.pojo.BaseAnalysisPojo;
import com.xzc.concurr.pojo.Result;

public class CollectUtil {

	//私有化构造器
	private CollectUtil(){
	}

	/**
	 * 初始化result对象的本地累积量
	 * @return 完成初始化后的result对象
	 */
	public static Result initLocalResult(){
		Result result = new Result();
		result.setBaseAnalysis(new BaseAnalysis());
		result.setTransmitDetails(new ArrayList<>());
		return result;
	}

	public static Result collectResult(Result localResult, Result result){

		//		collectBaseAnalysis(localBa, ba);

		return localResult;
	}



	/**
	 * 累积计算BaseAnalysis的数据
	 * @param localBa BaseAnalysis对象累积的本地量
	 * @param ba BaseAnalysis加入累积的新值对象
	 * @return 完成一次累积后的BaseAnalysis本地量对象
	 */
	public static BaseAnalysis collectBaseAnalysis(BaseAnalysis localBa, BaseAnalysis ba){
		Map<String, Map<String, Integer>> localTranCom = localBa.getTranCom();
		Map<String, Integer> localShuiJunMap = localBa.getShuiJunMap();
		Map<String, Integer> localExpressionMap = localBa.getExpressionMap();
		Map<String, Integer> localCerTypeMap = localBa.getCerTypeMap();
		Map<String, Integer> localKeyworkMap = localBa.getKeyworkMap();
		Map<String,Integer> localAreaMap = localBa.getAreaMap();
		Map<String,Integer> localUserFansMap = localBa.getUserFansMap();

		Map<String, Map<String, Integer>> tranCom = ba.getTranCom();
		Map<String, Integer> shuiJunMap = ba.getShuiJunMap();
		Map<String, Integer> expressionMap = ba.getExpressionMap();
		Map<String, Integer> cerTypeMap = ba.getCerTypeMap();
		Map<String, Integer> keyworkMap = ba.getKeyworkMap();
		Map<String,Integer> areaMap = ba.getAreaMap();
		Map<String,Integer> userFansMap = ba.getUserFansMap();

		localBa.setTranCom(cumulateTranCom(localTranCom, tranCom));
		localBa.setShuiJunMap(cumulateMap(localShuiJunMap, shuiJunMap));
		localBa.setExpressionMap(MapTool.getTopX(
				cumulateMap(localExpressionMap, expressionMap), 20)); //只获取前数值最高的前20个
		localBa.setCerTypeMap(cumulateMap(localCerTypeMap, cerTypeMap));
		localBa.setKeyworkMap(MapTool.getTopX(
				cumulateMap(localKeyworkMap, keyworkMap), 20)); //只获取前数值最高的前20个
		localBa.setAreaMap(cumulateMap(localAreaMap, areaMap));
		localBa.setUserFansMap(cumulateMap(localUserFansMap, userFansMap));
		return localBa;
	}


	/**
	 * 累积转发评论的结果
	 * @param localTranCom 本地的累积量map对象
	 * @param tranCom 最新收集的计算结果map对象
	 * @return 完成一次收集累积后的本地累积量map对象
	 */
	private static Map<String, Map<String, Integer>> cumulateTranCom(
			Map<String, Map<String, Integer>> localTranCom, 
			Map<String, Map<String, Integer>> tranCom){
		for(Map.Entry<String, Map<String, Integer>> entry: tranCom.entrySet()){
			String time = entry.getKey();
			if (localTranCom.containsKey(time)) {
				Map<String, Integer> localMap = localTranCom.get(time);
				Map<String, Integer> map = tranCom.get(time);
				localMap.put("评论数", localMap.get("评论数")+map.get("评论数"));
				localMap.put("转发数", localMap.get("转发数")+map.get("转发数"));
				localTranCom.put(time, localMap);
			} else {
				localTranCom.put(time, entry.getValue());
			}
		}
		return localTranCom;
	}


	/**
	 * Map<String, Integer>类型的map集合合并累加
	 * @param localMap 累加的本地量map对象
	 * @param map 累加的新值map对象
	 * @return 完成累加后的Map<String, Integer>对象
	 */
	private static Map<String, Integer> cumulateMap(Map<String, Integer> localMap,
			Map<String, Integer> map){

		for(Map.Entry<String, Integer> entry: map.entrySet()){
			String key = entry.getKey();
			Integer value = entry.getValue();
			if (localMap.containsKey(key)) {
				localMap.put(key,localMap.get(key)+value);
			} else {
				localMap.put(key, value);
			}
		}

		return localMap;
	}


	/**
	 * 处理BaseAnalysis对象中的表情和关键词属性，截取两者map中值最大的前10的元素
	 * @param ba 需要处理的BaseAnalysis对象
	 * @return 截取完成后的BaseAnalysis对象
	 */
	public static BaseAnalysis getTop10(BaseAnalysis ba){
		Map<String, Integer> expressionMap = ba.getExpressionMap();
		Map<String, Integer> keyworkMap = ba.getKeyworkMap();
		ba.setExpressionMap(MapTool.getTopX(expressionMap, 10));
		ba.setKeyworkMap(MapTool.getTopX(keyworkMap, 10));
		return ba;
	}


	/**
	 * 将BaseAnalysis对象转换为List<BaseAnalysisPojo>对象
	 * @param ba 需要转换的BaseAnalysis对象
	 * @return 完成转换的List<BaseAnalysisPojo>对象
	 */
	public static List<BaseAnalysisPojo> toBaseAnalysisPojoList(BaseAnalysis ba){
		List<BaseAnalysisPojo> list = new ArrayList<>();
		BaseAnalysisPojo bap = new BaseAnalysisPojo();
		bap.setOutId(ba.getAnalysisBlogId());
		bap.setTypeId(1);
		bap.setDate(TimeTool.getCurrentDate());
		bap.setDateTime(TimeTool.getCurrentDateTime());

		list.addAll(setList(bap, ba.getTranCom())); //转换转发评论
		list.addAll(setList(bap, renameShuiJun(ba.getShuiJunMap()), 2));//将固定key值换为中文
		list.addAll(setList(bap, ba.getExpressionMap(), 3));
		list.addAll(setList(bap, renameUerCerType(ba.getCerTypeMap()), 4));//将固定key值换为中文
		list.addAll(setList(bap, ba.getKeyworkMap(), 5));
		list.addAll(setList(bap, ba.getAreaMap(), 6));
		list.addAll(setList(bap, ba.getUserFansMap(), 7));
		return list;

	}

	/**
	 * 将{@code map}中的元素转换为List<BaseAnalysisPojo>，方便持久化
	 * @param inBap 已经封装了部分属性数据的BaseAnalysisPojo对象
	 * @param map 需要转换的 Map<String, Integer>对象
	 * @param type 需要封装内部BaseAnalysisPojo的属性analysisType的数据
	 * @return 转化完成的List<BaseAnalysisPojo>对象
	 */
	private static List<BaseAnalysisPojo> setList(BaseAnalysisPojo inBap,  
			Map<String, Integer> map, int type){
		List<BaseAnalysisPojo> list = new ArrayList<>();
		for(Map.Entry<String, Integer> entry: map.entrySet()){
			BaseAnalysisPojo bap = newIinitedBaseAnalysisPojo(inBap);
			bap.setAnalysisType(type);
			bap.setName(entry.getKey());
			bap.setCount(entry.getValue());
			list.add(bap);
		}
		return list;
	}


	/**
	 * 将Map<String, Map<String, Integer>> 类型{@code map}中的元素转换为List<BaseAnalysisPojo>，
	 * 方便持久化
	 * @param bap 已经封装了部分属性数据的BaseAnalysisPojo对象
	 * @param map 需要转换的 Map<String, Integer>对象
	 * @return 转化完成的List<BaseAnalysisPojo>对象
	 */
	private static List<BaseAnalysisPojo> setList(BaseAnalysisPojo inBap,  
			Map<String, Map<String, Integer>> map){
		List<BaseAnalysisPojo> list = new ArrayList<>();
		for(Map.Entry<String,  Map<String, Integer>> entry0 : map.entrySet()){
			for(Map.Entry<String, Integer> entry1: entry0.getValue().entrySet()){
				BaseAnalysisPojo bap = newIinitedBaseAnalysisPojo(inBap);
				bap.setAnalysisType(1);
				bap.setName(entry1.getKey());
				bap.setCount(entry1.getValue());
				bap.setDate(entry0.getKey());
				bap.setDateTime(entry0.getKey());
				list.add(bap);
			}
		}
		return list;
	}


	
	@Test
	public void test(){
		
		BaseAnalysisPojo bap = new BaseAnalysisPojo();
		bap.setOutId("11111111111111111");
		bap.setTypeId(1);
		bap.setDate(TimeTool.getCurrentDate());
		bap.setDateTime(TimeTool.getCurrentDateTime());
		
		
		Map<String, Integer> subMap0 = new HashMap<>();
		subMap0.put("转发数", 3);
		subMap0.put("评论数", 17);
		subMap0.put("shuijun", 34);
		
		
	}
	
	public static void main(String[] args) {
		BaseAnalysisPojo bap = new BaseAnalysisPojo();
		bap.setOutId("11111111111111111");
		bap.setTypeId(1);
		bap.setDate(TimeTool.getCurrentDate());
		bap.setDateTime(TimeTool.getCurrentDateTime());
		
		
		Map<String, Map<String, Integer>> map = new HashMap<>();
		Map<String, Integer> subMap0 = new HashMap<>();
		subMap0.put("转发数", 3);
		subMap0.put("评论数", 17);
		map.put("2014-09-22", subMap0);
		
		Map<String, Integer> subMap1 = new HashMap<>();
		subMap1.put("转发数", 12);
		subMap1.put("评论数", 8);
		map.put("2016-09-15", subMap1);
		
		List<BaseAnalysisPojo> list = setList(bap, map);
	}
	
	private static BaseAnalysisPojo newIinitedBaseAnalysisPojo(BaseAnalysisPojo inBap){
		BaseAnalysisPojo bap = new BaseAnalysisPojo();
		bap.setOutId(inBap.getOutId());
		bap.setTypeId(inBap.getTypeId());
		bap.setDate(inBap.getDate());
		bap.setDateTime(inBap.getDateTime());
		return bap;
	}

	/**
	 * 重命名用户认证类型，使用中文，灌入数据库
	 * @param map 需要重名的用户认证类型map
	 * @return 重命名后的用户认证类型map对象
	 */
	private static Map<String, Integer> renameUerCerType(Map<String, Integer> map){
		if (map.containsKey("comType")) {
			map.put("普通用户", map.get("comType"));
			map.remove("comType");
		}
		if (map.containsKey("cerType")) {
			map.put("个人认证", map.get("cerType"));
			map.remove("cerType");
		}
		if (map.containsKey("weiboArtType")) {
			map.put("微博达人", map.get("weiboArtType"));
			map.remove("weiboArtType");
		}
		if (map.containsKey("ofiCerType")) {
			map.put("官方认证", map.get("ofiCerType"));
			map.remove("ofiCerType");
		}
		return map;
	}
	
	/**
	 * 重命名水军用户与真实用户，使用中文，灌入数据库
	 * @param map 需要重名的水军map
	 * @return 重命名后的水军map对象
	 */
	private static Map<String, Integer> renameShuiJun(Map<String, Integer> map){
		if (map.containsKey("shuiJun")) {
			map.put("水军用户", map.get("shuiJun"));
			map.remove("shuiJun");
		}
		if (map.containsKey("realUser")) {
			map.put("真实用户", map.get("realUser"));
			map.remove("realUser");
		}
		return map;
	}
	

}
