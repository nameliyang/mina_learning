package com.ly.sun.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
	private static final Logger logger = LoggerFactory.getLogger(SimpleNioServerTest.class);
	public static void main(String[] args) {
		System.out.println(compute(10));
	}
	public static int compute(int num){
		if(num ==1){
			return 1;
		}
		return num * compute(num-1);
	}
}
