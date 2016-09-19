package com.xzc.concurr.pojo;


public class BaseAnalysis {
	
	private String outId; //博主ID
	private int typeId; //单条微博分析固定值为1
	private int analysisType; 
	private String name;
	private int count; //数目
	private String code; 
//	private Date date;
//	private Timestamp time;
	private String date;
	private String time;
	
	public String getOutId() {
		return outId;
	}
	public void setOutId(String outId) {
		this.outId = outId;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public int getAnalysisType() {
		return analysisType;
	}
	public void setAnalysisType(int analysisType) {
		this.analysisType = analysisType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
}
