package com.ly.sun.util;

public class NamePerservingRunnable implements Runnable {
	
	Runnable runnable;
	
	String threadName;
	
	public NamePerservingRunnable(Runnable runnable,String threadName){
		this.runnable = runnable;
		this.threadName = threadName;
	}
	
	@Override
	public void run() {
		String oldName = Thread.currentThread().getName();
		Thread.currentThread().setName(threadName);
		try{
			runnable.run();
		}finally{
			Thread.currentThread().setName(oldName);
		}
	}
	
}
