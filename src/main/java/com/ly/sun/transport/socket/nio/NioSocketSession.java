package com.ly.sun.transport.socket.nio;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.ly.sun.core.filterchain.IoFilterChain;

public class NioSocketSession {
	
	NioSocketAcceptor acceptor;
	
	NioProcessor processor;
	
	SocketChannel socketChannel ;
	
	SelectionKey selectKey;
	
	IoFilterChain ioFilterChain;
	
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

	public void setIoFilterChain(IoFilterChain ioFilterChain) {
		this.ioFilterChain = ioFilterChain;
	}
	
}
