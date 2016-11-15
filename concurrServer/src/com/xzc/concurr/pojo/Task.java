package com.xzc.concurr.pojo;

import java.io.Serializable;

public class Task<T> implements Serializable{

	private static final long serialVersionUID = 1L;
	private Object obj; //任务对象
	private int taskObjId; //任务对象ID
	private int iTask; //第i个任务
	private int sumTask; //总任务
	private boolean flag; //任务状态false为完成，true完成
	
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	public int getiTask() {
		return iTask;
	}
	public int getTaskObjId() {
		return taskObjId;
	}
	public void setTaskObjId(int taskObjId) {
		this.taskObjId = taskObjId;
	}
	public void setiTask(int iTask) {
		this.iTask = iTask;
	}
	public int getSumTask() {
		return sumTask;
	}
	public void setSumTask(int sumTask) {
		this.sumTask = sumTask;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	
}
