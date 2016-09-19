package com.xzc.concurrServer.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Entity;

import com.xzc.concurr.pojo.BaseAnalysis;

public class ConvertUtil {

	/**
	 * 	//计算地区
		areaMap = parseArea(userJson, areaMap);
		//计算用户类型	
		userTypeMap = parseUerType(userJson, userTypeMap);
		//计算粉丝
		rang = parseFans(userJson, rang);
		//计算水军
		shuiJunCount = parseShuiJun(userJson, shuiJunCount);
	 */
	List<BaseAnalysis> baList = new ArrayList<BaseAnalysis>();
	BaseAnalysis baseAnalysis = new BaseAnalysis();
	
	//convert to 
	
	/**
	 * 把计算好的地区areaMap转化为BaseAnalysis对象的list
	 * @param areaMap 需要转化地区map
	 * @return 转化后的BaseAnalysis的list
	 */
	public List<BaseAnalysis> toBaseAnalysisAreaMap(Map<String, Integer> areaMap) {
		List<BaseAnalysis> baList = new ArrayList<BaseAnalysis>();
		for(Map.Entry<String, Integer> entry : areaMap.entrySet()){
			
			String key = entry.getKey();
			Integer value = entry.getValue();
			BaseAnalysis baseAnalysis = new BaseAnalysis();
			baseAnalysis.setAnalysisType(6);
			baseAnalysis.setName(key);
			baseAnalysis.setCount(value);
			//blogId 先别设置
//			baseAnalysis.setOutId(outId);
			baList.add(baseAnalysis);
		}
		return baList;
		
	}
	
	
	
	/**
	 * 把用户类型计算结果userTypeMap转化成为BaseAnalysis对象的list
	 * @param userTypeMap 需要转化地区map
	 * @return 转化后的BaseAnalysis的list
	 */
	public List<BaseAnalysis> toBaseAnalysisUserType(Map<String, Integer> userTypeMap) {
		List<BaseAnalysis> baList = new ArrayList<BaseAnalysis>();
		for(Map.Entry<String, Integer> entry : userTypeMap.entrySet()){
			String key = entry.getKey();
			Integer value = entry.getValue();
			BaseAnalysis baseAnalysis = new BaseAnalysis();
			baseAnalysis.setAnalysisType(4);
			baseAnalysis.setName(key);
			baseAnalysis.setCount(value);
			//blogId 先别设置
//			baseAnalysis.setOutId(outId);
			baList.add(baseAnalysis);
		}
		return baList;
		
	}
	
	
	
	/**
	 * 把用户类型计算结果userTypeMap转化成为BaseAnalysis对象的list
	 * @param userTypeMap 需要转化地区map
	 * @return 转化后的BaseAnalysis的list
	 */
	public List<BaseAnalysis> toBaseAnalysisFans(int[] rang) {
		List<BaseAnalysis> baList = new ArrayList<BaseAnalysis>();
		
		
		
		
		
		
		return baList;
		
	}
	
	
	
	
	
	//
}
