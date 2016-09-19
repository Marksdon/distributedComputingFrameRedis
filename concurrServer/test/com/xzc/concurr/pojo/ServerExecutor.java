package com.xzc.concurr.pojo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xzc.concurrServer.men.Boss;

public class ServerExecutor {
	
	public static void main(String[] args) {
		ServerExecutor executor = new ServerExecutor();
		executor.startup();
	}
	
	public void startup () {
		ExecutorService service = Executors.newFixedThreadPool(10);
		service.execute(new Producer());
		service.execute(new Boss());
	}
	
}
