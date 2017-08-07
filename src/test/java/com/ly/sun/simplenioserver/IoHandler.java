package com.ly.sun.simplenioserver;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;

public class IoHandler {
	private static Logger logger = LoggerFactory.getLogger(IoHandler.class);
	
	
	public void onReadData(NioSession session, ByteBuffer buffer) throws IOException{
		try{
			String readStr = readBufferAsString(buffer);
			logger.info("sessionId = {},read msg={}",session.getSessionId(),readStr);
			session.write(ByteBuffer.wrap(readStr.toUpperCase().getBytes()));
		}finally{
			buffer.clear();
		}
	}
	
	public void sessionCreated(NioSession session){
		logger.info("sessionId = {} created",session.getSessionId());
	}
	
	public String readBufferAsString(ByteBuffer buffer){
		StringBuilder sb = new StringBuilder();
		while(buffer.hasRemaining()){
			sb.append((char)buffer.get());
		}
		return sb.toString();
	}
	
}
