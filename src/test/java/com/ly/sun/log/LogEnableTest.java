package com.ly.sun.log;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogEnableTest {
	
	private static final Logger logger = LoggerFactory.getLogger(LogEnableTest.class);
	
	public static void main(String[] args) throws InterruptedException {
		logger.error("{} {} {} {} {}",new Object[]{"hello",1,new Date(),1,
			"sdfasdfsdddddddddddddddddddddddddddddddddddddd"});
		
 		for(;;){
// 			logger.info("{} {} ",
// 					"17/06/26 22:02:41 ERROR log.LogEnableTest: hello 1 Mon Jun 26 22:02:41 GMT+08:00 2017 1 sdfasdfsdddddddddddddddddddddddddddddddddddddd",
// 					"17/06/26 22:02:41 ERROR log.LogEffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7 1 sdfasdfsdddddddddddddddddddddddddddddddddddddd");
 			
			logger.info(
 					"17/06/26 22:02:41 ERROR log.LogEnableTest: hello 1 Mon Jun 26 22:02:41 GMT+08:00 2017 1 sdfasdfsdddddddddddddddddddddddddddddddddddddd"+
 					"17/06/26 22:02:41 ERROR log.LogEffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7 1 sdfasdfsdddddddddddddddddddddddddddddddddddddd");

 			Thread.sleep(10);
 		}
	}
}
