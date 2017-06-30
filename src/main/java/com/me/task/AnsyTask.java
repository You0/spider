package com.me.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnsyTask {
	private static ExecutorService executorService = Executors.newFixedThreadPool(1);
	
	public static void runTask(Runnable runnable){
		executorService.submit(runnable);
	}
	
}
