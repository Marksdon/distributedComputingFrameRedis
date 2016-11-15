package com.xzc.concurrServer.men;

import java.util.HashMap;
import java.util.Map;

import com.xzc.concurr.pojo.Result;
import com.xzc.concurr.result.CollectionPool;
import com.xzc.concurr.result.RegistorCollection;
import com.xzc.concurr.result.ResultCumulatePool;
import com.xzc.concurrServer.util.RedisUtilSyn;
import com.xzc.concurrServer.util.SerializeUtil;
import com.xzc.concurrServer.util.ThreadUtil;

import redis.clients.jedis.Jedis;

public class Boss implements Runnable {

	@Override
	public void run() {
		Jedis jedis = null;
		Map<String, Result> map = new HashMap<>(); //结果池
		RegistorCollection rc = new RegistorCollection(); //登记池
		ResultCumulatePool rCPool = new ResultCumulatePool();//累计池
		CollectionPool cp = new CollectionPool(map, rc);//初始化
		while (true) {
			try {
				jedis = RedisUtilSyn.getJedis(1);
//				byte[] bArr = jedis.lpop("resultQueueTest".getBytes());
				byte[] bArr = jedis.lpop("resultQueueLocalTest".getBytes());
				if (bArr == null) {
					System.err.println("bArr == null");
					ThreadUtil.slowThread();
				} else {
					Result result = (Result)SerializeUtil.unserialize(bArr);
					cp = rCPool.testContain(cp, result);
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
