package com.ly.sun.core.future;

public interface IoFuture {
	
	
	boolean  isDone();
	
	
	IoFuture await() throws InterruptedException;
	
	
	
	
	
	
}
