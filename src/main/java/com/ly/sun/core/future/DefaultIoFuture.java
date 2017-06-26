package com.ly.sun.core.future;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultIoFuture implements IoFuture{

	private static final Logger logger = LoggerFactory.getLogger(DefaultIoFuture.class);
	
	private volatile boolean isDone = false;
	
	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public IoFuture await() throws InterruptedException {
		return null;
	}

	@Override
	public boolean await(long timeout) throws InterruptedException {
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
