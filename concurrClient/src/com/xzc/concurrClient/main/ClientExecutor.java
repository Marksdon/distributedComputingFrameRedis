package com.xzc.concurrClient.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xzc.concurrClient.men.Consumer;

public class ClientExecutor {

	public static void main(String[] args) {
		ClientExecutor executor = new ClientExecutor();
		executor.startup();
	}

	public void startup(){
		ExecutorService service = Executors.newFixedThreadPool(10);
		for(int i = 0; i < 10; i ++) {
			service.execute(new Consumer());
		}
	}
}
