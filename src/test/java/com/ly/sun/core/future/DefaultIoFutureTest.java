package com.ly.sun.core.future;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultIoFutureTest {
	private static final Logger logger = LoggerFactory.getLogger(DefaultIoFutureTest.class);
	static final DefaultIoFuture future = new DefaultIoFuture();
	
	public static void main(String[] args) throws IOException {
		FutureThread thread = new FutureThread();
		thread.start();
		
		System.in.read();
		logger.info("set future value....");
		thread.interrupt();
		//future.setValue("hello");
	}
	
	static class FutureThread extends Thread{
		@Override
		public void run() {
			logger.info("start future thread");
			boolean isDone = false;
//			try {
				isDone = future.awaitUninterruptibly(10000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			logger.info("isDone ={}",isDone);
			logger.info("end the future thread");
		}
	}
}
