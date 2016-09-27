package com.xzc.concurrServer.men;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xzc.concurr.pojo.BaseAnalysis;
import com.xzc.concurr.pojo.BaseAnalysisPojo;
import com.xzc.concurr.pojo.Result;
import com.xzc.concurr.pojo.TransmitDetail;
import com.xzc.concurr.relationship.Relationship;
import com.xzc.concurr.result.RegistorCollection;
import com.xzc.concurr.result.ResultRegistor;
import com.xzc.concurrServer.db.SqlWorker;
import com.xzc.concurrServer.util.CollectUtil;
import com.xzc.concurrServer.util.JDBCUtil;
import com.xzc.concurrServer.util.RedisUtilSyn;
import com.xzc.concurrServer.util.SerializeUtil;
import com.xzc.concurrServer.util.SqlConfig;
import com.xzc.concurrServer.util.ThreadUtil;

import redis.clients.jedis.Jedis;

public class Boss implements Runnable {

	@Override
	public void run() {

		Jedis jedis = null;
		RegistorCollection rc = new RegistorCollection();
		ResultRegistor registor = null;
		
		Map<String, Result> map = new HashMap<>();//结果池,未实现
		
//		Result localResult = CollectUtil.initLocalResult();
		BaseAnalysis localBa = new BaseAnalysis();
		List<TransmitDetail> localTd = new ArrayList<>();
		Connection conn = null;
		while (true) {
			try {
				jedis = RedisUtilSyn.getJedis(1);
				byte[] bArr = jedis.lpop("resultQueueTest".getBytes());
				if (bArr == null) {
					System.err.println("bArr == null");
					ThreadUtil.slowThread();
				} else {
					Result result = (Result)SerializeUtil.unserialize(bArr);
					localTd.addAll(result.getTransmitDetails());//还没有做大小的限制
					if ((registor = rc.collecteResult(result)) != null) {//该项计算完成
						
						BaseAnalysis ba = result.getBaseAnalysis();
						localBa.setAnalysisBlogId(registor.getResultId());
						localBa = CollectUtil.collectBaseAnalysis(localBa, ba);
						localBa = CollectUtil.getTop10(localBa);//截取BaseAnalysis中的关键字和表情的前10
						//计算完成，可以入库处理
						System.out.println(registor.getResultId() + " 收集完成，可以入库处理");
						
						List<TransmitDetail> finalTds = 
								Relationship.buildRelationship(localTd, localTd.get(0));//随意的
						
						
						List<BaseAnalysisPojo> list = CollectUtil.toBaseAnalysisPojoList(localBa);
						conn = JDBCUtil.getMySQLConnection(SqlConfig.url);
						SqlWorker.SaveBaseAnalysisListTest(conn, list);
						SqlWorker.saveTransDetailList(conn, finalTds);
						showValue(localBa);
						localBa = new BaseAnalysis();//销毁旧对象
						localTd.clear();
					} else {
						BaseAnalysis ba = result.getBaseAnalysis();
						localBa = CollectUtil.collectBaseAnalysis(localBa, ba); 
						//未完成收集计算结果，继续汇总统计
						System.out.println("未完成收集计算结果，继续汇总统计");
					}
					System.out.println("正在收集结果项数： " + rc.remain());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (jedis != null) {
					RedisUtilSyn.returnResource(jedis);
				}
				JDBCUtil.close(conn);
			}
		}

	}
	
	
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
