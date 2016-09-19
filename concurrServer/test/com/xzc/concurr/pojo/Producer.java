package com.xzc.concurr.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.xzc.concurr.pojo.Bloger;
import com.xzc.concurr.pojo.Task;
import com.xzc.concurrServer.util.RedisUtilSyn;
import com.xzc.concurrServer.util.SerializeUtil;

import redis.clients.jedis.Jedis;


public class Producer implements Runnable {

	private static AtomicInteger count = new AtomicInteger();

	@Override
	public void run() {
		Jedis jedis = null;
		System.out.println("count's initial value : " + count.intValue());
		while (count.intValue() < 10) {
			try {
				Bloger bloger = new Bloger();
				Task task = setTask(bloger);

				jedis = RedisUtilSyn.getJedis(1);
				jedis.rpush("taskQueueTest".getBytes(),
						SerializeUtil.serialize(task));
				System.out.println("produce task: " + task.getiTask());
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (jedis != null) {
					RedisUtilSyn.returnResource(jedis);
				}
			}
		}
		System.out.println("count's value : " + count.intValue());

	}


	/**
	 * 一个博主对象就分发一个任务队列
	 * @param blogger 博主对象
	 * @return 博主的任务队列
	 * @throws InterruptedException 
	 */
	public static BlockingQueue<Task> assignTask(Object obj) 
			throws InterruptedException {
		BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
		//get tatol
		//没有实现
		int tatol = 10;
		for(int i = 1; i <= tatol; i ++){
			Task task = new Task();
			task.setObj(obj);
			task.setiTask(i);
			task.setSumTask(i);
			//			taskQueue.add(task);
			if (!taskQueue.offer(task, 2, TimeUnit.SECONDS)) {
				System.out.println("fail to get in the queue");
			}
		}
		return taskQueue;
	}

	/**
	 * 遍历队列元素,特别注意的是队列的遍历元素是动态的，所以别用for循环来读队列的size做边界
	 * @param taskQueue
	 */
	public static void travelQueue(BlockingQueue<Task> taskQueue){

		while (!taskQueue.isEmpty()) {
			try {
				Task task = taskQueue.take();
				System.out.println(task.getObj() + "|" + task.getiTask() + "|"
						+ task.getSumTask());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}


	/**
	 * 设置一个task对象
	 * @param obj 传进来需要加入任务的对象
	 * @return 返回设置好的任务对线task
	 */
	public static Task setTask(Object obj){
		Task task = new Task();
		task.setObj(obj);
		task.setiTask(count.incrementAndGet());
		task.setSumTask(10);
		return task;
	}

	/**
	 * 设置任务，把传进来的对象设置成为任务
	 * @param obj 传进来需要计算的对象
	 * @return 一个list的任务对象task
	 */
	public static List<Task> setTasks(Object obj){
		//获得总任务数,假设是m
		int m = 10; //假设的总任务数
		List<Task> list = new ArrayList<>();
		for(int i = 0; i < m; i ++){
			Task task = new Task();
			task.setObj(obj);
			task.setiTask(count.incrementAndGet());
			task.setSumTask(10);
			list.add(task);
		}
		return list;
	}




}
