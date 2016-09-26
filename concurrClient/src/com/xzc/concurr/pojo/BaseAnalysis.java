package com.xzc.concurr.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BaseAnalysis implements Serializable {

	static final long serialVersionUID = 1L;
	private String analysisBlogId;
	//1.转发评论
	private Map<String, Map<String, Integer>> tranCom;
	//2.用户类型（水军用户 ，真实用户）
	private Map<String, Integer> shuiJunMap;
	//3.表情统计
	private Map<String, Integer> expressionMap;
	//4.用户认证类型
	private Map<String, Integer> cerTypeMap;
	//5.关键字
	private Map<String, Integer> keyworkMap;
	//6.用户地区分布
	private Map<String,Integer> areaMap;
	//7.用户粉丝分析
	private Map<String,Integer> userFansMap;
	
	
	public BaseAnalysis() {
		tranCom = new HashMap<>();
		shuiJunMap = new HashMap<>();
		shuiJunMap.put("shuiJun", 0);
		shuiJunMap.put("realUser", 0);
		
		expressionMap = new HashMap<>();
		cerTypeMap = new HashMap<>();
		cerTypeMap.put("comType", 0);
		cerTypeMap.put("cerType", 0);
		cerTypeMap.put("weiboArtType", 0);
		cerTypeMap.put("ofiCerType", 0);
		
		keyworkMap = new HashMap<>();
		areaMap = new HashMap<>();
		userFansMap = new HashMap<>();
		for (int i = 0; i < 10; i++) {
			userFansMap.put("rang"+i, 0);
		}
	}
	
	
	public String getAnalysisBlogId() {
		return analysisBlogId;
	}
	public void setAnalysisBlogId(String analysisBlogId) {
		this.analysisBlogId = analysisBlogId;
	}
	public Map<String, Map<String, Integer>> getTranCom() {
		return tranCom;
	}
	public void setTranCom(Map<String, Map<String, Integer>> tranCom) {
		this.tranCom = tranCom;
	}
	public Map<String, Integer> getShuiJunMap() {
		return shuiJunMap;
	}
	public void setShuiJunMap(Map<String, Integer> shuiJunMap) {
		this.shuiJunMap = shuiJunMap;
	}
	public Map<String, Integer> getExpressionMap() {
		return expressionMap;
	}
	public void setExpressionMap(Map<String, Integer> expressionMap) {
		this.expressionMap = expressionMap;
	}
	public Map<String, Integer> getCerTypeMap() {
		return cerTypeMap;
	}
	public void setCerTypeMap(Map<String, Integer> cerTypeMap) {
		this.cerTypeMap = cerTypeMap;
	}
	public Map<String, Integer> getKeyworkMap() {
		return keyworkMap;
	}
	public void setKeyworkMap(Map<String, Integer> keyworkMap) {
		this.keyworkMap = keyworkMap;
	}
	public Map<String, Integer> getAreaMap() {
		return areaMap;
	}
	public void setAreaMap(Map<String, Integer> areaMap) {
		this.areaMap = areaMap;
	}
	public Map<String, Integer> getUserFansMap() {
		return userFansMap;
	}
	public void setUserFansMap(Map<String, Integer> userFansMap) {
		this.userFansMap = userFansMap;
	}
	
}
