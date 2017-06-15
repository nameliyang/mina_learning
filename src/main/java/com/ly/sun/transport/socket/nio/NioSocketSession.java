package com.ly.sun.transport.socket.nio;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

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
	
	private ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();	
	
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
	
	
	public Object getAttribute(Object key){
		return attributes.get(key);
	}
	
	public void setAttribute(Object key,Object value){
		attributes.put(key, value);
	}
	
	public void setAttributeIfAbsent(Object key,Object value){
		  attributes.putIfAbsent(key, value);
	}
	class SessionConfigImpl extends AbstractIoSessionConfig{
		
	}
}
