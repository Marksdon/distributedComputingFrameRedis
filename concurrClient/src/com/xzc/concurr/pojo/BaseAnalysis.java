package com.xzc.concurr.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BaseAnalysis implements Serializable {

	static final long serialVersionUID = 1L;
	
	private int transmits;//转发数
	private int comments;//评论数
	private int fansCount;
	private int manCount;
	private int fameCount;
	private String comType = "普通用户";
	private String cerType = "个人认证";
	private String weiboArtType = "微博达人";
	private String OfiCerType = "官方认证";
	private int comTypeCount;
	private int cerTypeCount;
	private int weiboArtTypeCount;
	private int OfiCerTypeCount;
	private int shuiJunCount;
	//转发用户类型
	private Map<String,Integer> userTypeMap = new HashMap<>();
	//转发用户客户端来源
	private Map<String,Integer> sourceMap = new HashMap<>();
	//转发用户地区分布
	private Map<String,Integer> areaMap = new HashMap<>();
	//转发用户粉丝分析
	private Map<String,Integer> userFansMap = new HashMap<>();
	// 表情统计
	private Map<String, Integer> expressionMap = new HashMap<>();
	//关键字
	private Map<String, Integer> keyworkMap = new HashMap<>();

	private Map<String, Map<String, Integer>> tranCom = new HashMap<String, Map<String, Integer>>();

	public int getTransmits() {
		return transmits;
	}

	public void setTransmits(int transmits) {
		this.transmits = transmits;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public int getFansCount() {
		return fansCount;
	}

	public void setFansCount(int fansCount) {
		this.fansCount = fansCount;
	}

	public int getManCount() {
		return manCount;
	}

	public void setManCount(int manCount) {
		this.manCount = manCount;
	}

	public int getFameCount() {
		return fameCount;
	}

	public void setFameCount(int fameCount) {
		this.fameCount = fameCount;
	}

	public String getComType() {
		return comType;
	}

	public void setComType(String comType) {
		this.comType = comType;
	}

	public String getCerType() {
		return cerType;
	}

	public void setCerType(String cerType) {
		this.cerType = cerType;
	}

	public String getWeiboArtType() {
		return weiboArtType;
	}

	public void setWeiboArtType(String weiboArtType) {
		this.weiboArtType = weiboArtType;
	}

	public String getOfiCerType() {
		return OfiCerType;
	}

	public void setOfiCerType(String ofiCerType) {
		OfiCerType = ofiCerType;
	}

	public int getComTypeCount() {
		return comTypeCount;
	}

	public void setComTypeCount(int comTypeCount) {
		this.comTypeCount = comTypeCount;
	}

	public int getCerTypeCount() {
		return cerTypeCount;
	}

	public void setCerTypeCount(int cerTypeCount) {
		this.cerTypeCount = cerTypeCount;
	}

	public int getWeiboArtTypeCount() {
		return weiboArtTypeCount;
	}

	public void setWeiboArtTypeCount(int weiboArtTypeCount) {
		this.weiboArtTypeCount = weiboArtTypeCount;
	}

	public int getOfiCerTypeCount() {
		return OfiCerTypeCount;
	}

	public void setOfiCerTypeCount(int ofiCerTypeCount) {
		OfiCerTypeCount = ofiCerTypeCount;
	}

	public int getShuiJunCount() {
		return shuiJunCount;
	}

	public void setShuiJunCount(int shuiJunCount) {
		this.shuiJunCount = shuiJunCount;
	}

	public Map<String, Integer> getUserTypeMap() {
		return userTypeMap;
	}

	public void setUserTypeMap(Map<String, Integer> userTypeMap) {
		this.userTypeMap = userTypeMap;
	}

	public Map<String, Integer> getSourceMap() {
		return sourceMap;
	}

	public void setSourceMap(Map<String, Integer> sourceMap) {
		this.sourceMap = sourceMap;
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

	public Map<String, Integer> getExpressionMap() {
		return expressionMap;
	}

	public void setExpressionMap(Map<String, Integer> expressionMap) {
		this.expressionMap = expressionMap;
	}

	public Map<String, Integer> getKeyworkMap() {
		return keyworkMap;
	}

	public void setKeyworkMap(Map<String, Integer> keyworkMap) {
		this.keyworkMap = keyworkMap;
	}

	public Map<String, Map<String, Integer>> getTranCom() {
		return tranCom;
	}

	public void setTranCom(Map<String, Map<String, Integer>> tranCom) {
		this.tranCom = tranCom;
	}

	
	
	
	
}
