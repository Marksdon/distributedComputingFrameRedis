package com.xzc.concurrServer.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xzc.concurrServer.men.Boss;
import com.xzc.concurrServer.men.Producer;

/**
 * the executor class of the distributed computing frame
 * @author randall
 *
 */
public class ServerExecutor {
	
	public static void main(String[] args) {
		ServerExecutor executor = new ServerExecutor();
		executor.startup();
	}
	
	public void startup () {
		ExecutorService service = Executors.newFixedThreadPool(10);
		service.execute(new Producer());
//		service.execute(new Boss());
	}
	
}
