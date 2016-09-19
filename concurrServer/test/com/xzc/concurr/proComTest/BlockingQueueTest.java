package com.xzc.concurr.proComTest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueTest {

	public static void main(String[] args) throws InterruptedException {
		// 声明一个容量为10的缓存队列
		/**
		 * 1.jvm 在方法区没有找到BlockingQueue类信息，就在CLASSPATH中找到BlockingQueue,
		 * 通过类加载器载入方法区(代码信息{1.一堆属性，方法，静态变量和方法，常量池（内存中共享）}
		 * 2.在栈中画出变量queue的地址空间
		 * 3.调用构造方法LinkedBlockingQueue（）new出对象LinkedBlockingQueue（加载和刚才一样）
		 * 4.把初始化后的LinkedBlockingQueue对象的首地址给queue，让queue引用（指向）
		 * LinkedBlockingQueue对象
		 */
		
		BlockingQueue<Object> queue = new LinkedBlockingQueue<Object>(10);
		Producer producer1 = new Producer(queue);
		Producer producer2 = new Producer(queue);
		Producer producer3 = new Producer(queue);
		Consumer consumer = new Consumer(queue);
		
		// 借助Executors
		ExecutorService service = Executors.newCachedThreadPool();
		// 启动线程
		service.execute(producer1);
		service.execute(producer2);
		service.execute(producer3);
		service.execute(consumer);
		
		// 执行10s
		Thread.sleep(10 * 1000);
//		producer1.stop();
//		producer2.stop();
//		producer3.stop();

		Thread.sleep(2000);
		// 退出Executor
		service.shutdown();
	}
}
