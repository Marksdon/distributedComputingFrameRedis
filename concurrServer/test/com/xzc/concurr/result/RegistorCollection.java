package com.xzc.concurr.result;

import java.util.HashMap;
import java.util.Map;

import com.xzc.concurr.pojo.Result;

/**
 * 收集计算结果对象的集合，如果收集计算完成则返回一个标记该项计算结果的ResultRegistor对象，否则返回null
 * @author randall
 *
 */
public class RegistorCollection {

	//装载ResultRegistor对象
	private Map<String, ResultRegistor> map;

	//初始化容器
	public RegistorCollection() {
		map = new HashMap<>();
	}


	/**
	 * 统计收集计算结果{@code result}，放到集合中(hashMap),
	 * 传入的某项任务如果收集完成就返回该项计算结果的ResultRegistor对象
	 * @param result 需要收集的Result对象
	 * @return 如果{@code result}全部收集完成就返回对应的ResultRegistor，否则返回null
	 */
	public ResultRegistor collecteResult(Result result){
		ResultRegistor rr = null;
		if (!map.containsKey(result.getResultId())) {
			rr = initResultRegistor(result);
			map.put(result.getResultId(), rr);
		} else {
			rr = map.get(result.getResultId());
			if (result.isFlag()) {
				rr.setSuccessNum(rr.getSuccessNum() + 1);
			} else {
				rr.setFailNum(rr.getFailNum() + 1);
			}

		}
		if (isDone(rr)) {
			map.remove(rr.getResultId());//收集完成，移出map
			return rr;
		}
		return null;

	}

	/**
	 * 返回当前集合所剩余的结果收集任务数
	 * @return 当前集合没有收集完成的结果项数
	 */
	public int remain(){
		return map.size();
	}


	/**
	 * 初始化ResultRegistor对象
	 * @param result 穿进来的结果对象
	 * @return 初始化后的ResultRegistor对象
	 */
	private ResultRegistor initResultRegistor(Result result){
		ResultRegistor rr = new ResultRegistor();
		rr.setResultId(result.getResultId());
		rr.setTotalNum(result.getSumResult());
		if (result.isFlag()) {
			rr.setSuccessNum(1);
		} else {
			rr.setFailNum(1);
		}
		return rr;
	}

	/**
	 * 判断该项任务的收集回来的结果是否完成
	 * @param rr ResultRegistor对象
	 * @return ture 如果计算成功和计算失败的数等于总任务数，否则返回false
	 */
	private boolean isDone(ResultRegistor rr){
		int total = rr.getTotalNum();
		int failNum = rr.getFailNum();
		int successNum = rr.getSuccessNum();
		return (total == failNum+successNum) ? true : false; 
	}
}
