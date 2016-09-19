package com.xzc.concurrServer.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.xzc.concurr.pojo.Result;

public class ResultCollector {

	private Map<String, ResultCollector> map;

	private String resultId;
	private int successNum;
	private int failNum;
	private int sum;

	private ResultCollector() {
		inti();
	}


	private void inti(){
		successNum = 0;
		failNum = 0;
		sum = failNum + successNum;
	}


	/**
	 * List<Result> 
	 * if(contain) result's successNum++ otherwise failNum++
	 * 	if ((successNum + fail) == sum) mission done
	 */

	
	private String collecteResult(Result result){
		String key = result.getResultId();
		if (map.containsKey(key)) {
			ResultCollector resultCollector = map.get(key);
			resultCollector.resultId = result.getResultId();
			if (result.isFlag()) {
				resultCollector.successNum ++;
			} else {
				resultCollector.failNum ++;
			}
		} else {
			ResultCollector rc = new ResultCollector();
			if (result.isFlag()) {
				rc.successNum ++;
			} else {
				rc.failNum ++;
			}
			map.put(result.getResultId(), rc);
		}
		sum = failNum + successNum;

		if (sum == result.getSumResult()) {
			return resultId;
		} 
		return null;

	}


	public void testCollection(Result result){
		String key = result.getResultId();
		Map<String, ResultCollector> map = new HashMap<>();
		int successNum = 0;
		int failNum = 0;
		//对应进来
		if (map.containsKey(key)) {
			ResultCollector resultCollector = map.get(key);
			resultCollector.resultId = result.getResultId();
			if (result.isFlag()) {
				successNum = ++ resultCollector.successNum;
			} else {
				failNum = ++ resultCollector.failNum ;
			}
			if ((successNum + failNum) == result.getSumResult()) {
				//syso(done)
				//处理这项任务的结果，入库或者
			}
		} else {
			ResultCollector rc = new ResultCollector();
			if (result.isFlag()) {
				rc.successNum ++;
			} else {
				rc.failNum ++;
			}
			map.put(result.getResultId(), rc);
		}

	}


	
	
	/**
	 * 内存变量地址没有变，堆里对象属性变化而已
	 */
	@Test
	public void testMapValue(){
		Result result = new Result();
		Map<String, ResultCollector> map = new HashMap<>();
		ResultCollector rc = new ResultCollector();
		rc.failNum = 4;
		rc.successNum = 5;
		map.put("key0", rc);

		if (map.containsKey("key0")) {
			ResultCollector resultCollector = map.get("key0");
			resultCollector.failNum ++;
		}

		ResultCollector resultCollector = map.get("key0");
		System.out.println(resultCollector.failNum 
				+ "|" + resultCollector.successNum);

	}



}
