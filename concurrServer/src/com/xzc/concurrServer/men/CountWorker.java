package com.xzc.concurrServer.men;

import com.xzc.concurr.pojo.Result;
import com.xzc.concurr.pojo.Task;
import com.xzc.concurrServer.util.CountUtil;

/**
 * 计算任务的类
 * @author randall
 *
 */
public class CountWorker {
	
	/**
	 * 计算单位-单个博主
	 * 1.翻页请求数据
	 * 2.单页解析数据
	 * 3.封装成为对象--一个博主一个对象
	 */
	
	/**
	 * 传进来一个blogger对象，计算完成后转化为一个Result对象
	 * @param blogger 传进来的blogger对象
	 * @return result 计算完成后的Result对象
	 */
	public static Result count(Task task){
		//count detail
		
		/**
		 * 类型,微博分析(1-转发评论数，2-用户类型，3-表情，4-认证类型，5-关键词)
		 * 1.基本类型对象--baseAnalysis
		 * 2.转发对象 --transmitDetail
		 * 前面两个对象组合为一个对象 result
		 * 
		 */
		
		//计算基本数据
		//转化为基本类型对象
		
		Result result = new Result();
		
		//请求数据
		String json = "";
		
		CountUtil cu = new CountUtil();
		
		String user = "";
		cu.countUserSerial(user);		
/*		//关键词
		cu.parseKeyword(text, map);
		//表情
		cu.parseExpression(text, map);
		//转发评论
		cu.parseTranCom(text, date, map);*/
		//
		//转化为对象

		return null;
	}
	
	
	
	
}
