package com.xzc.concurrServer.men;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.xzc.concurr.interfaces.Producable;
import com.xzc.concurr.pojo.Task;
import com.xzc.concurr.pojo.Blogger;
import com.xzc.concurrServer.db.ConnectionPool;
import com.xzc.concurrServer.db.SqlWorker;
import com.xzc.concurrServer.util.RedisUtilSyn;
import com.xzc.concurrServer.util.SerializeUtil;

import redis.clients.jedis.Jedis;

public class Producer implements Producable {

	private static AtomicInteger count = new AtomicInteger();

	@Override
	public void run(){
		System.out.println("count's initial value : " + count.intValue());
		ConnectionPool pool = null;
		try {
			/*初始化连接池*/
			pool =  ConnectionPool.getInstance();
			if (pool == null) {
				System.out.println("pool == null");
			}
			while (true) {
				try {
					Connection conn = pool.getConnection();
					if (conn == null) {
						System.out.println("conn == null");
						pool.closePool();
						break;
					}
					//获得数据
					List<Blogger> sourceList = SqlWorker.getDateSource(conn);
					pool.release(conn);//返还conn回连接池

					for (Blogger taskObj : sourceList) {
						//设置任务对象属性
						List<Task> taskList = setTasks(taskObj);
						//分配任务
						assignTasksTestNotNullJedis(taskList);
					}
					
					//任务分配完成后，呼叫GC清空一下sourceList里面的对象
					sourceList.clear();
					System.out.println("sourceList is empty: " + sourceList.isEmpty());

					//线程沉睡10秒
					Thread.sleep(10000);
				} catch (Exception e) {
					e.printStackTrace();
					//logger
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pool != null) {
				pool.closePool();
			}
		}


	}

	/**
	 * 分配任务到任务队列
	 * @param taskList 设置好的任务对象task的list
	 */
	public static void assignTasks(List<Task> taskList){
		AtomicInteger count = new AtomicInteger();
		Jedis jedis = null;
		try {
			jedis = RedisUtilSyn.getJedis(1);
			if (jedis != null) {
				for (Task task : taskList) {
					try {
						jedis.rpush("taskQueueTest".getBytes(),
								SerializeUtil.serialize(task));
						System.out.println("produce task: " + task.getiTask());
						count.incrementAndGet();
					} catch (Exception e) {
						e.printStackTrace();
					} 

				}
			}
		} finally {
			if (jedis != null) {
				RedisUtilSyn.returnResource(jedis);
			}
			System.out.println("count's value : " + count.intValue());
		}

	}


	/**
	 * 测试使用一个非空的jedis对象方法
	 * @param taskList
	 */
	public static void assignTasksTestNotNullJedis(List<Task> taskList){
		AtomicInteger count = new AtomicInteger();
		Jedis jedis = RedisUtilSyn.getJedis(); //返回的jedis对象一定非空
		try {
			for (Task task : taskList) {
				try {
//					jedis.rpush("taskQueueTest".getBytes(),
					jedis.rpush("taskQueueLocalTest".getBytes(),
							SerializeUtil.serialize(task));
					System.out.println("produce task: " + task.getiTask());
					count.incrementAndGet();
				} catch (Exception e) {
					e.printStackTrace();
				} 

			}
		} finally {
			if (jedis != null) {
				RedisUtilSyn.returnResource(jedis);
			}
			System.out.println("count's value : " + count.intValue());
		}

	}


	/**
	 * 下面的AtomicInteger对象使用要注意，分配完成任务要重新设置数值
	 */

	/**
	 * 设置一个task对象
	 * @param obj 传进来需要加入任务的对象
	 * @return 返回设置好的任务对线task
	 */
	public Task setTask(Object obj){
		Task task = new Task();
		task.setObj(obj);
		task.setiTask(count.incrementAndGet());
		task.setSumTask(50);
		return task;
	}

	/**
	 * 设置任务，把传进来的对象设置成为任务
	 * @param obj 传进来需要计算的对象
	 * @return 一个list的任务对象task
	 */
	public List<Task> setTasks(Object obj){
		//获得总任务数,假设是m
		/*int m = 10;*/ //假设的总任务数
		Blogger blogger = (Blogger)obj;
		int m = processSum(blogger.getTransmits()); //暂时先用这个数值
		List<Task> list = new ArrayList<>();
		for(int i = 1; i <= m; i ++){
			Task task = new Task();
			task.setObj(blogger.getBlogId());
			task.setiTask(i);
			task.setSumTask(m);
			list.add(task);
		}
		return list;
	}

	/**
	 * 设置一个task对象
	 * @param obj 传进来需要加入任务的对象
	 * @return 返回设置好的任务对线task
	 */
	public Task setTask(int taskObjId){
		Task task = new Task();
		task.setTaskObjId(taskObjId);
		task.setiTask(count.incrementAndGet());
		task.setSumTask(50);
		return task;
	}


	/**
	 * 设置任务，把传进来的对象设置成为任务
	 * @param obj 传进来需要计算的对象
	 * @return 一个list的任务对象task
	 */
	public List<Task> setTasks(int taskObjId){
		//获得总任务数,假设是m
		int m = 10; //假设的总任务数
		List<Task> list = new ArrayList<>();
		for(int i = 0; i < m; i ++){
			Task task = new Task();
			task.setTaskObjId(taskObjId);
			task.setiTask(i);
			task.setSumTask(10);
			list.add(task);
		}
		return list;
	} 

	/**
	 * 分配任务总数
	 * @param m 需要分割的数值
	 * @return 返回的任务总数
	 */
	private int processSum(int m){
		return (m%20 == 0) ? m/20 : (m/20+1);
	}

}
