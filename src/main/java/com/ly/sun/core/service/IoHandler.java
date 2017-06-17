package com.ly.sun.core.service;

import com.ly.sun.core.session.IoSession;

public interface IoHandler {
	
	void sessionCreated(IoSession session) throws Exception;
	
	void messageReceived(IoSession session,Object msg) throws Exception;
	
	
}
