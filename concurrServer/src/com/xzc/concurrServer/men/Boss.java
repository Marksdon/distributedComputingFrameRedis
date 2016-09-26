package com.xzc.concurrServer.men;

import com.xzc.concurr.pojo.Result;
import com.xzc.concurr.result.RegistorCollection;
import com.xzc.concurr.result.ResultRegistor;
import com.xzc.concurrServer.util.RedisUtilSyn;
import com.xzc.concurrServer.util.SerializeUtil;
import com.xzc.concurrServer.util.ThreadUtil;

import redis.clients.jedis.Jedis;

public class Boss implements Runnable {

	@Override
	public void run() {

		Jedis jedis = null;
		RegistorCollection rc = new RegistorCollection();
		ResultRegistor registor = null;
		while (true) {
			try {
				jedis = RedisUtilSyn.getJedis(1);
				byte[] bArr = jedis.lpop("resultQueueTest".getBytes());
				if (bArr == null) {
					//
					System.err.println("bArr == null");
					ThreadUtil.slowThread();
				} else {
					Result result = (Result)SerializeUtil.unserialize(bArr);
					if ((registor = rc.collecteResult(result)) != null) {//该项计算完成
						//计算完成，可以入库处理
						System.out.println(registor.getResultId() + " 收集完成，可以入库处理");
					} else {
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
			}
		}

	}

}
