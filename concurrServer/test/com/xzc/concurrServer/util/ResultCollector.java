package com.xzc.concurrServer.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xzc.concurr.pojo.Result;
import com.xzc.concurr.result.ResultRegistor;

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
	 * 收集回来的数据要到一个map集合中报到
	 * @param map Map<String, ResultRegistor> map的一个对象
	 * @param result 收集回来的result对象
	 * @return 登记后的map对象
	 */
	public Map<String, ResultRegistor> registryResult(
			Map<String, ResultRegistor> map, Result result){

		String resultId = result.getResultId();

		if (map.containsKey(resultId)) {
			ResultRegistor rr = map.get(resultId);
			if (result.isFlag()) {
				rr.setSuccessNum(rr.getSuccessNum()+1);
				//汇总工作
			} else {
				rr.setFailNum(rr.getFailNum()+1);
			}
		} else {
			ResultRegistor registor = new ResultRegistor();
			registor.setResultObj(resultId);
			registor.setTotalNum(result.getSumResult());
			if (result.isFlag()) {
				registor.setSuccessNum(1);
			} else {
				registor.setFailNum(1);
			}
			map.put(resultId, registor);
		}
		return map;

	}
	
	

	/**
	 * 检查map中是否已经得到
	 * @param map
	 * @return
	 */
	public Object getDone(Map<String, ResultRegistor> map){
		int failNum = 0;
		int successNum = 0;
		int totalNum = 0;
		List<Object> list = new ArrayList<>();
		for(Map.Entry<String, ResultRegistor> entry: map.entrySet()){
			ResultRegistor registor = entry.getValue();
			failNum = registor.getFailNum();
			successNum = registor.getSuccessNum();
			totalNum = registor.getTotalNum();
			if ((failNum + successNum) == totalNum) {
				list.add(entry.getKey());
				map.remove(entry.getKey());
			}
		}
		return list;
	}


}
