package com.xzc.concurr.interfaces;

import java.util.List;

import com.xzc.concurr.pojo.Task;

public interface Producable extends Runnable {
	
	/**
	 * 将传进来的{@code obj}对象配置任务对象task
	 * @param obj 需要计算的对象
	 * @return 配置完成的任务对象task
	 */
	public Task setTask(Object obj);
	
	/**
	 * 将传进来的{@code obj}对象配置任务对象task
	 * @param obj 需要计算的对象
	 * @return 配置完成的任务对象task的一个list
	 */
	public List<Task> setTasks(Object obj);
	
	/**
	 * 将传进来的{@code taskOjbId} 对象ID设置为任务对象
	 * @param taskObjId 需要计算的对象id号
	 * @return 配置完成的任务对象task
	 */
	public Task setTask(int taskObjId);
	
	/**
	 * 将传进来的{@code taskOjbId} 对象ID设置为任务对象
	 * @param taskObjId 需要计算的对象id号
	 * @return 配置完成的任务对象task的一个list
	 */
	public List<Task> setTasks(int taskObjId);
	
}
