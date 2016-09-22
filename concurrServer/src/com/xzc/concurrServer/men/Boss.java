package com.xzc.concurrServer.men;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.xzc.concurr.pojo.Result;
import com.xzc.concurrServer.util.RedisUtilSyn;
import com.xzc.concurrServer.util.SerializeUtil;
import com.xzc.concurrServer.util.ThreadUtil;

import redis.clients.jedis.Jedis;

public class Boss implements Runnable {

	@Override
	public void run() {
		int sucess = 0; 
		int fail = 0;
		Jedis jedis = null;
		AtomicInteger count = new AtomicInteger(0);

		while (true) {
			try {
				jedis = RedisUtilSyn.getJedis(1);
				byte[] bArr = jedis.lpop("resultQueueTest".getBytes());

				if (bArr == null && count.get() != 0) {
					ThreadUtil.slowThread();
					System.err.println("barr is null on Boss");
				} else if(bArr != null) {
					Result result = (Result)SerializeUtil.unserialize(bArr);
					System.out.println("get result: " + result.getiResult());
					Map<String, Integer> map = sumTask(result, sucess, fail);
					sucess = map.get("sucess");
					fail = map.get("fail");
					System.out.println("sucess:" + sucess + "| fail:"+ fail);
					
					if (isDone(result, sucess, fail)) {
						//得到最终结果
						System.out.println("=========Got the final result!=========");
						sucess = 0;
						fail = 0;
						System.out.println("=========sets sucess = " + sucess + ", fail = " + fail + " =========");
					}
					count.incrementAndGet();
				}
				if (count.get() == 50000) {
					System.out.println("set value again : " + count.getAndSet(1));
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


	/**
	 * 模拟计算结果的汇总
	 * @param result 计算结果
	 * @param sucess 任务成功累计量
	 * @param fail 任务失败累计量
	 * @return 任务成功和失败的累计量，封装在一个map中
	 */
	public Map<String, Integer> sumTask(Result result, int sucess, int fail ){
		//判断任务状态
		if (result.isFlag()) {
			//任务计算成功
			sucess ++;
		} else {
			//任务计算失败
			fail ++;
		}

		Map<String, Integer> map = new HashMap<>();
		map.put("sucess", sucess);
		map.put("fail", fail);
		return map;
	}
	
	
	/**
	 * 判断任务是否计算完成
	 * @param result 计算结果对象
	 * @param sucess 成功的累计量
	 * @param fail 失败的累计量
	 * @return true 计算完成，否者false 没有计算完成
	 */
	public boolean isDone(Result result, int sucess, int fail){
		return (sucess + fail) == result.getSumResult();
	}
	
	
	

}
