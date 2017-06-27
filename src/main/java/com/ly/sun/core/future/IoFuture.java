package com.ly.sun.core.future;

import java.util.concurrent.TimeUnit;

public interface IoFuture {
	
	boolean  isDone();
	
	IoFuture await() throws InterruptedException;
	
	boolean await(long timeout) throws InterruptedException;
	
	boolean await(long timeout,TimeUnit timeunit) throws InterruptedException;
	
	IoFuture awaitUninterruptibly();
	
	boolean awaitUninterruptibly(long timeoutMillis);
	
	
}
