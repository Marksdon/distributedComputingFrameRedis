package com.xzc.concurr.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class Result implements Serializable{
	private static final long serialVersionUID = 1L;
	private String resultId; //结果号
	private BaseAnalysis baseAnalysis; //基础分析
	private ArrayList<TransmitDetail> transmitDetails;//转发的list，ArrayList可序列化
	private int iResult;  //第i个结果
	private int sumResult;//总的结果
	private boolean flag; //结果状态，false表示任务完成，true任务失败
	public String getResultId() {
		return resultId;
	}
	public void setResultId(String resultId) {
		this.resultId = resultId;
	}
	public int getiResult() {
		return iResult;
	}
	public void setiResult(int iResult) {
		this.iResult = iResult;
	}
	public int getSumResult() {
		return sumResult;
	}
	public void setSumResult(int sumResult) {
		this.sumResult = sumResult;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public BaseAnalysis getBaseAnalysis() {
		return baseAnalysis;
	}
	public void setBaseAnalysis(BaseAnalysis baseAnalysis) {
		this.baseAnalysis = baseAnalysis;
	}
	public ArrayList<TransmitDetail> getTransmitDetails() {
		return transmitDetails;
	}
	public void setTransmitDetails(ArrayList<TransmitDetail> transmitDetails) {
		this.transmitDetails = transmitDetails;
	}
	
	
	
	
}
