package com.ly.sun.core.future;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultIoFuture implements IoFuture{
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultIoFuture.class);
	
	private volatile boolean ready = false;
	
	private static final Object lock = new Object();
	
	private static final Long DEFAULT_TIME_OUT = 1000L;
	
	private  volatile Object value;
	
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
	
	public Object getValue(){
		return value;
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
		return wait0(timeout,true);
	}

	private boolean wait0(long timeout,boolean isInterrupt) throws InterruptedException {
		synchronized (lock) {
			long endTime = System.currentTimeMillis() + timeout;
			logger.debug("end time={}",endTime);
			while(!ready){
				timeout = Math.min(timeout, DEFAULT_TIME_OUT);
				logger.debug("wait timeout={}",timeout);
				try{
					lock.wait(timeout);
				}catch(InterruptedException e){
					if(isInterrupt){
						throw e;
					}
				}
				logger.debug("leave timeout time={}",System.currentTimeMillis());
				if(System.currentTimeMillis() >= endTime || ready){
					break;
				}
			}
			return ready;
		}
	}

	@Override
	public boolean await(long timeout, TimeUnit timeunit)
			throws InterruptedException {
		return await(timeunit.toMillis(timeout));
	}

	@Override
	public IoFuture awaitUninterruptibly() {
		synchronized (lock) {
			while(!ready){
				try {
					lock.wait(DEFAULT_TIME_OUT);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return this;
		}
	}

	@Override
	public boolean awaitUninterruptibly(long timeoutMillis) {
		try {
			return wait0(timeoutMillis,false);
		} catch (InterruptedException e) {
		}
		return false;
	}

}
