package com.ly.sun.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.sun.core.session.IoSession;

public   class IoHandlerAdapter implements IoHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(IoHandlerAdapter.class);
	
	@Override
	public void messageReceived(IoSession session, Object msg) throws Exception {
		
	}
	
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		
	}
}
