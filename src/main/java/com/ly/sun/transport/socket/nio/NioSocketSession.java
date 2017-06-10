package com.ly.sun.transport.socket.nio;

import java.nio.channels.SocketChannel;

public class NioSocketSession {
	
	NioProcessor processor;
	
	public NioSocketSession(NioSocketAcceptor nioSocketAcceptor,
			NioProcessor processor, SocketChannel ch) {
		this.processor = processor;
	}
	
	public NioProcessor getProcessor(){
		return this.processor;
	}
	
}
