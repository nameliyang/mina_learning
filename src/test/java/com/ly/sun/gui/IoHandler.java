package com.ly.sun.gui;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoHandler {
	private static Logger logger = LoggerFactory.getLogger(IoHandler.class);
	
	public void onReadData(NioSession session, ByteBuffer buffer){
		try{
			String readStr = readBufferAsString(buffer);
			logger.info("sessionId = {},read msg={}",session.getSessionId(),readStr);
		}finally{
			buffer.clear();
		}
	}
	
	public String readBufferAsString(ByteBuffer buffer){
		StringBuilder sb = new StringBuilder();
		while(buffer.hasRemaining()){
			sb.append((char)buffer.get());
		}
		return sb.toString();
	}
	
}
