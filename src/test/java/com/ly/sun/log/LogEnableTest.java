package com.ly.sun.log;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogEnableTest {
	
	private static final Logger logger = LoggerFactory.getLogger(LogEnableTest.class);
	
	public static void main(String[] args) throws InterruptedException {
		LoggerTestThread thread1 = new LoggerTestThread();
		LoggerTestThread thread2 = new LoggerTestThread();
		thread1.start();
		thread2.start();
	}
	private static final String str1 = "17/06/26 22:02:41 ERROR log.LogEnableTest: hello 1 Mon Jun 26 22:02:41 GMT+08:00 2017 1 sdfasdfsdddddddddddddddddddddddddddddddddddddd";
	private static final String str2 = "17/06/26 22:02:41 ERROR log.LogEffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7 1 sdfasdfsdddddddddddddddddddddddddddddddddddddd";


	private static class LoggerTestThread extends Thread{
		@Override
		public void run() {
			for(;;){
	 			logger.info("{} {} ",str1,str2);
//				logger.info(
//	 					"17/06/26 22:02:41 ERROR log.LogEnableTest: hello 1 Mon Jun 26 22:02:41 GMT+08:00 2017 1 sdfasdfsdddddddddddddddddddddddddddddddddddddd"+
//	 					"17/06/26 22:02:41 ERROR log.LogEffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7 1 sdfasdfsdddddddddddddddddddddddddddddddddddddd");
	 			try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	 		}
		}
	}
}
