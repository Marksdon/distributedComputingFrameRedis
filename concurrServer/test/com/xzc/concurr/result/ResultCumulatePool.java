package com.xzc.concurr.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.xzc.concurr.pojo.BaseAnalysis;
import com.xzc.concurr.pojo.Result;
import com.xzc.concurr.pojo.TransmitDetail;
import com.xzc.concurr.relationship.Relationship;
import com.xzc.concurr.test.SourceSearcher;
import com.xzc.concurrServer.db.SqlWorker;
import com.xzc.concurrServer.util.CollectUtil;

/**
 * 提供一个池来计算累计结果
 * @author randall
 *
 */
public class ResultCumulatePool {

	//装在结果对象result
	private Map<String, Result> map;

	//初始化容器
	public ResultCumulatePool() {
		map = new HashMap<>();
	}


	/**
	 * 累计计算多个结果项的，如果某个结果项计算完成，将自动入库
	 * @param cp CollectionPool对象
	 * @param result 新收集的结果对象，需要封装到新的结果池中
	 * @return 更新完后的CollectionPool对象
	 */
	public CollectionPool contain(CollectionPool cp, Result result){

		/**
		 * 1.新结果对象进来，，如果池中没有，不管3721直接接入结果池中
		 * 2.登记池汇总判断是否收集完毕，完毕--最后一次汇总，没有>直接计算
		 */


		/**
		 * 1.先查看登记池，是否计算完成，完成>进入结果池中汇总，然后入库
		 * 					       未完成>进入结果集汇总
		 * 
		 */

		ResultRegistor registor = null;
		Map<String, Result> map = cp.getMap();
		RegistorCollection rc = cp.getRc();

		String resultId = result.getResultId();//先将结果对象ID号取出
		if (!map.containsKey(resultId)) {
			map.put(resultId, result);
		} 

		Result localResult = map.get(resultId); //匹配到池中有给ID
		BaseAnalysis localBa = localResult.getBaseAnalysis();
		ArrayList<TransmitDetail> localTd = localResult.getTransmitDetails();

		localTd.addAll(result.getTransmitDetails());//还没有做大小的限制
		if ((registor = rc.collecteResult(result)) != null) {//该项计算完成

			BaseAnalysis ba = result.getBaseAnalysis();
			localBa.setAnalysisBlogId(registor.getResultId());
			localBa = CollectUtil.collectBaseAnalysis(localBa, ba);
			localBa = CollectUtil.getTop10(localBa);//截取BaseAnalysis中的关键字和表情的前10
			//计算完成，可以入库处理
			System.out.println(registor.getResultId() + " 收集完成，可以入库处理");

			ArrayList<TransmitDetail> finalTds = 
					Relationship.buildRelationship(localTd, localTd.get(0));//随意的

			//完成最后一次result对象的封装
			localResult = encapsulateResult(finalTds, localBa, localResult);

			SqlWorker.saveResult(localResult);//结果入库
			showValue(localBa);
			//从结果池中移除
			map.remove(registor.getResultId());
			localBa = new BaseAnalysis();//销毁旧对象
			localTd.clear();
		} else {
			BaseAnalysis ba = result.getBaseAnalysis(); //获取新的result对象
			localBa = CollectUtil.collectBaseAnalysis(localBa, ba); //新的result对象与本地量累计

			//把新的属性数据封装一次
			localResult = 
					encapsulateResult(localTd, localBa, localResult);

			map.put(resultId, localResult);//把结果池更新一遍

			//未完成收集计算结果，继续汇总统计
			System.out.println("未完成收集计算结果，继续汇总统计");
		}
		System.out.println("正在收集结果项数： " + rc.remain());

		//返回结果池，登记池
		CollectionPool pool = new CollectionPool();
		pool.setMap(map);
		pool.setRc(rc);
		return pool;

	}




	/**
	 * 测试方法
	 * @param cp
	 * @param result
	 * @return
	 */
	public CollectionPool testContain(CollectionPool cp, Result result){

		/**
		 * 1.新结果对象进来，，如果池中没有，不管3721直接接入结果池中
		 * 2.登记池汇总判断是否收集完毕，完毕--最后一次汇总，没有>直接计算
		 */


		/**
		 * 思路2
		 * 1.先查看登记池，是否计算完成，完成>进入结果池中汇总，然后入库
		 * 					       未完成>进入结果集汇总
		 * 
		 */

		ResultRegistor registor = null;
		Map<String, Result> map = cp.getMap();
		RegistorCollection rc = cp.getRc();

		String resultId = result.getResultId();//先将结果对象ID号取出

		Result localResult = null;
		BaseAnalysis localBa = null;
		ArrayList<TransmitDetail> localTd = null;

		if ((registor = rc.collecteResult(result)) != null) {//该项计算完成
			if (!map.containsKey(resultId)) {
				localBa = new BaseAnalysis();
				localTd = new ArrayList<>();
				BaseAnalysis ba = result.getBaseAnalysis();
				localBa.setAnalysisBlogId(registor.getResultId());
				localBa = CollectUtil.collectBaseAnalysis(localBa, ba);
				localBa = CollectUtil.getTop10(localBa);//截取BaseAnalysis中的关键字和表情的前10
				localTd.addAll(result.getTransmitDetails());//还没有做大小的限制
			} else {
				localResult = map.get(resultId); //匹配到池中有给ID
				localBa = localResult.getBaseAnalysis();
				localTd = localResult.getTransmitDetails();
				BaseAnalysis ba = result.getBaseAnalysis();
				localBa.setAnalysisBlogId(registor.getResultId());
				localBa = CollectUtil.collectBaseAnalysis(localBa, ba);
				localBa = CollectUtil.getTop10(localBa);//截取BaseAnalysis中的关键字和表情的前10
				localTd.addAll(result.getTransmitDetails());//还没有做大小的限制
			}

			//计算完成，可以入库处理
			System.out.println(registor.getResultId() + " 收集完成，可以入库处理");

			TransmitDetail sourceId = 
					SourceSearcher.getTransSource(registor.getResultId());
			
			ArrayList<TransmitDetail> finalTds = 
					Relationship.buildRelationship(localTd, sourceId);

			//完成最后一次result对象的封装
			localResult = encapsulateResult(finalTds, localBa, localResult);

			SqlWorker.saveResult(localResult);//结果入库
			showValue(localBa);
			//从结果池中移除
			map.remove(registor.getResultId());
			localBa = new BaseAnalysis();//销毁旧对象
			localTd.clear();
		} else {

			if (!map.containsKey(resultId)) {
				map.put(resultId, result);
			} else {
				localResult = map.get(resultId); //匹配到池中有给ID
				localBa = localResult.getBaseAnalysis();
				localTd = localResult.getTransmitDetails();

				BaseAnalysis ba = result.getBaseAnalysis(); //获取新的result对象
				localBa = CollectUtil.collectBaseAnalysis(localBa, ba); //新的result对象与本地量累计
				localBa = CollectUtil.getTop10(localBa);//截取BaseAnalysis中的关键字和表情的前10
				localTd.addAll(result.getTransmitDetails());//还没有做大小的限制

				//把新的属性数据封装一次
				localResult = 
						encapsulateResult(localTd, localBa, localResult);

				map.put(resultId, localResult);//把结果池更新一遍

				//未完成收集计算结果，继续汇总统计
				System.out.println("未完成收集计算结果，继续汇总统计");
			}

		}
		System.out.println("正在收集结果项数： " + rc.remain());

		//返回结果池，登记池
		CollectionPool pool = new CollectionPool();
		pool.setMap(map);
		pool.setRc(rc);
		return pool;

	}




	/**
	 * 封装result对象的transmitDetails属性和transmitDetails
	 * @param tds 封装的transmitDetails属性对象
	 * @param ba 封装的transmitDetails属相对象
	 * @param result 封装的result对象
	 * @return 完成封装的result对象
	 */
	private static Result encapsulateResult(
			ArrayList<TransmitDetail> tds, BaseAnalysis ba, Result result){
		result.setBaseAnalysis(ba);
		result.setTransmitDetails(tds);
		return result;

	}


	/**
	 * 返回当前集合所剩余的结果收集任务数
	 * @return 当前集合没有收集完成的结果项数
	 */
	public int remain(){
		return map.size();
	}


	/**
	 * for test
	 * @param ba
	 */
	private void showValue(BaseAnalysis ba){
		System.out.println(ba.getTranCom());
		System.out.println(ba.getShuiJunMap());
		System.out.println(ba.getExpressionMap());//
		System.out.println(ba.getCerTypeMap());
		System.out.println(ba.getKeyworkMap());//
		System.out.println(ba.getAreaMap());
		System.out.println(ba.getUserFansMap());
	}


}
