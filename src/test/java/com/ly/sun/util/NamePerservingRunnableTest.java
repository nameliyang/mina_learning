package com.ly.sun.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NamePerservingRunnableTest {
	
	public static void main(String[] args) {
		
		ExecutorService pool = Executors.newFixedThreadPool(3);
		pool.execute(new NamePerservingRunnable(new TestThread(), "A"));
		pool.execute(new NamePerservingRunnable(new TestThread(), "B"));
		pool.execute(new NamePerservingRunnable(new TestThread(), "C"));
		pool.execute(new NamePerservingRunnable(new TestThread(), "D"));
		pool.execute(new NamePerservingRunnable(new TestThread(), "E"));
	}
	
	static class TestThread implements Runnable{
		
		public TestThread(){
			
		}
		
		@Override
		public void run() {
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName());
		}
		
	}
}


