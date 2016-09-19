package com.xzc.concurrClient.men;

import com.xzc.concurr.pojo.Result;
import com.xzc.concurr.pojo.Task;
import com.xzc.concurrClient.util.RedisUtilSyn;
import com.xzc.concurrClient.util.SerializeUtil;
import com.xzc.concurrClient.util.ThreadUtil;

import redis.clients.jedis.Jedis;

public class Consumer implements Runnable{

	@Override
	public void run() {
		Task task;
		Result result;
		Jedis jedis = null;

		while (true) {
			try {
				jedis = RedisUtilSyn.getJedis(1);
				byte[] bArr = jedis.lpop("taskQueueTest".getBytes());

				if (bArr == null) {
					//暂停2秒
					ThreadUtil.slowThread();
					System.out.println("barr is null on consumer");
				} else if (bArr != null){
					task = (Task)SerializeUtil.unserialize(bArr);
					System.out.println("consume task: " + task.getiTask());
					//计算任务
					result = countTask(task);
					//计算结果进入队列
					jedis.rpush("resultQueueTest".getBytes(),
							SerializeUtil.serialize(result));
					System.out.println("rpush one: " + result.getiResult());
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
	 * 模拟计算成功的任务
	 * 模拟计算任务，无论任务计算成功还是失败，都返回一个任务的计算结果对象result
	 * @param task 传入的任务对象task
	 * @return 计算结果对象result
	 */
	public Result countSucessTask(Task task){
		Result result = new Result();
		result.setResultId(task.getTaskObjId()+"");
		result.setiResult(task.getiTask());
		result.setSumResult(task.getSumTask());
		result.setFlag(true);
		return result;
	}


	/**
	 * 模拟计算失败任务，无论任务计算成功还是失败，都返回一个任务的计算结果对象result
	 * @param task 传入的任务对象task
	 * @return 计算结果对象result
	 */
	public Result countFailTask(Task task){
		Result result = new Result();
		result.setResultId(task.getTaskObjId()+"");
		result.setiResult(task.getiTask());
		result.setSumResult(task.getSumTask());
		result.setFlag(false);
		return result;
	}


	/**
	 * 模拟计算任务
	 * @param task 需要计算的任务
	 * @return 任务计算完成后的结果对象result
	 */
	public Result countTask(Task task) {
		Result result = null;
		//计算任务
		if (task.getiTask() == 3 ) {
			//模拟任务计算失败
			result = countFailTask(task);
		} else {
			//模拟任务计算成功
			result = countSucessTask(task);
		}
		return result;
	}


}
