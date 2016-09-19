package com.xzc.concurrClient.util;

/**
 * 该类提供线程控制的方法
 * @author randal
 *
 */
public class ThreadUtil {
	
	/**
	 * 将线程暂停1秒
	 */
	public static void slowThread() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
