package com.ly.sun.transport.socket.nio;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.ly.sun.core.filterchain.IoFilterChain;
import com.ly.sun.core.session.AbstractIoSessionConfig;
import com.ly.sun.core.session.IoSessionConfig;

public class NioSocketSession {
	
	NioSocketAcceptor acceptor;
	
	NioProcessor processor;
	
	SocketChannel socketChannel ;
	
	SelectionKey selectKey;
	
	IoFilterChain ioFilterChain;
	
	private  IoSessionConfig sessionConfig = new SessionConfigImpl();
	
	
	public NioSocketSession(NioSocketAcceptor nioSocketAcceptor,
			NioProcessor processor, SocketChannel ch) {
		this.processor = processor;
		this.acceptor = nioSocketAcceptor;
		this.socketChannel = ch;
	}
	
	public NioProcessor getProcessor(){
		return this.processor;
	}

	public SocketChannel getChannel() {
		return socketChannel;
	}

	public void setSelectionKey(SelectionKey selectKey) {
		this.selectKey = selectKey;
	}
	
	public SelectionKey getSelectionKey(){
		return selectKey;
	}

	public void setIoFilterChain(IoFilterChain ioFilterChain) {
		this.ioFilterChain = ioFilterChain;
	}
	
	public IoSessionConfig getSessionConfig(){
		return sessionConfig;
	}
	
	class SessionConfigImpl extends AbstractIoSessionConfig{
		
	}
}
