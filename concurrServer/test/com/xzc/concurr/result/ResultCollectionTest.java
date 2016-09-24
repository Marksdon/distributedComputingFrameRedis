package com.xzc.concurr.result;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.xzc.concurr.pojo.Result;

/**
 * 测试计算结果收集
 * @author randall
 */

public class ResultCollectionTest {

	@Test
	public void testResultCollection(){
		RegistorCollection rc = new RegistorCollection();
		ResultRegistor registor = null;
		for(int i = 1; i <= 15; i ++){
			Result result = new Result();
			result = setResult(result,"aaa", i, 10);
			if (i == 3 || i == 5 || i == 7 || i == 12 || i == 13) {
				result = setResult(result,"bbb", i-10, 5);
			}
			if ((registor = rc.collecteResult(result)) != null) {//该项计算完成
				//计算完成，可以入库处理
				System.out.println(registor.getResultId() + " 收集完成，可以入库处理");
			} else {
				//未完成收集计算结果，继续汇总统计
				System.out.println("未完成收集计算结果，继续汇总统计");
			}
			System.out.println("正在收集结果项数： " + rc.remain());
		}

	}


	/**
	 * 设置Result对象属性
	 * @param result 需要设置属性的Result对象
	 * @param i 第i个结果
	 * @param sum 总的结果数
	 * @return 设置好属性的Result对象
	 */
	private Result setResult(Result result, String resultId,
			int i, int sum){
		result.setResultId(resultId);
		result.setFlag(true);
		result.setiResult(i);
		result.setSumResult(sum);
		return result;
	}

	
	
	@Test
	public void testMapSize(){
		Map<String, String> map = new HashMap<>();
		System.out.println("init, size : " + map.size());
		map.put("a", "aaa");
		map.put("b", "bbb");
		
		map.remove("a");
		
		System.out.println("after remove, size : " + map.size());
	}

}
