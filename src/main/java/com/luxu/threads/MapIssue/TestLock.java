package com.luxu.threads.MapIssue;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestLock {
	private static final ExecutorService pool = Executors.newFixedThreadPool(5);

	private HashMap map = new HashMap();

	public TestLock() {
		for(int j=0;j<1000;j++){
			pool.execute(()->{
				for (int i = 0; i < 50000; i++) {
					map.put(new Integer(i), i);
				}
			});
			pool.execute(()->{
				for (int i = 0; i < 50000; i++) {
					map.get(new Integer(i));
				}
			});
		}
		pool.shutdown();
	}

	public static void main(String[] args) {
		new TestLock();
	}
}