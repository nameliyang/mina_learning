package com.ly.sun.simplenioserver;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;

public class IoHandler {
	private static Logger logger = LoggerFactory.getLogger(IoHandler.class);
	
	StringBuilder sb = new StringBuilder();
	
	public void onReadData(NioSession session, ByteBuffer buffer) throws IOException{
		try{
			String readStr = readBufferAsString(buffer);
			logger.info("sessionId = {},read msg={}",session.getSessionId(),readStr);
			sb.append(readStr);
			if(sb.toString().endsWith("exit")){
				session.write(ByteBuffer.wrap(sb.toString().getBytes()));
			}
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
