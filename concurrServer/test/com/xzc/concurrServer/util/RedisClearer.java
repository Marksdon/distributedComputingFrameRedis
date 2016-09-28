package com.xzc.concurrServer.util;

import com.xzc.concurr.pojo.Result;
import com.xzc.concurr.pojo.Task;

import redis.clients.jedis.Jedis;

public class RedisClearer {

	/**
	 * 工具类一般不给实例化
	 */
	private RedisClearer() {
	}
	
	public static void main(String[] args) {
		clearTaskQueue();//清除任务队列里元素
//		clearResultQueue();//清除结果队列里元素
	}

	/**
	 * 清除任务队列的里的元素
	 */
	public static void clearTaskQueue(){
		Jedis jedis = null;
		Task task = null;
		try {
			jedis = RedisUtilSyn.getJedis();
			while (true) {
				byte[] barr = jedis.lpop("taskQueueTest".getBytes());
				if (barr == null) {
					System.out.println("barr is null on taskQueueTest");
				} else {
					task = (Task)SerializeUtil.unserialize(barr);
					System.out.println(
							task.getiTask() + "|" + task.getSumTask());
				}
			}

		} finally {
			if (jedis != null) {
				RedisUtilSyn.returnResource(jedis);
			}
		}

	}
	
	
	
	/**
	 * 清除结果队列里的元素
	 */
	public static void clearResultQueue(){
		Jedis jedis = null;
		Result result = null;
		try {
			jedis = RedisUtilSyn.getJedis();
			while (true) {
				byte[] barr = jedis.lpop("resultQueueTest".getBytes());
				if (barr == null) {
					System.out.println("barr is null on resultQueueTest");
				} else {
					result = (Result)SerializeUtil.unserialize(barr);
					System.out.println(
							result.getResultId() + "|" + result.getiResult());
				}
			}

		} finally {
			if (jedis != null) {
				RedisUtilSyn.returnResource(jedis);
			}
		}

	}
}
