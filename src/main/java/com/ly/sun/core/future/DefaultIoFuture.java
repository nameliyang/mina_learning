package com.ly.sun.core.future;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultIoFuture implements IoFuture{
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultIoFuture.class);
	
	private volatile boolean ready = false;
	
	private static final Object lock = new Object();
	
	private static final Long DEFAULT_TIME_OUT = 1000L;
	
	private Object value;
	
	@Override
	public boolean isDone() {
		return ready;
	}
	
	public void setValue(Object value){
		this.value = value;
		ready = true;
		synchronized (lock) {
			lock.notifyAll();	
		}
	}
	
	
	@Override
	public IoFuture await() throws InterruptedException {
		synchronized (lock) {
			while(!ready){
				lock.wait(DEFAULT_TIME_OUT);
			}
			return this;
		}
	}

	@Override
	public boolean await(long timeout) throws InterruptedException {
		synchronized (lock) {
			while(!ready){
				
			}
		}
		return false;
	}

	@Override
	public boolean await(long timeout, TimeUnit timeunit)
			throws InterruptedException {
		return false;
	}

	@Override
	public IoFuture awaitUninterruptibly() {
		return null;
	}

	@Override
	public boolean awaitUninterruptibly(long timeoutMillis) {
		return false;
	}

}
