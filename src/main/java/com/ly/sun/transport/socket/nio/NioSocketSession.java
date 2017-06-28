package com.ly.sun.transport.socket.nio;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

import com.ly.sun.core.filterchain.IoFilterChain;
import com.ly.sun.core.future.DefaultWriteFuture;
import com.ly.sun.core.future.WriteFuture;
import com.ly.sun.core.service.IoHandler;
import com.ly.sun.core.session.AbstractIoSessionConfig;
import com.ly.sun.core.session.IoSession;
import com.ly.sun.core.session.IoSessionConfig;

public class NioSocketSession implements IoSession {
	
	NioSocketAcceptor acceptor;
	
	NioProcessor processor;
	
	SocketChannel socketChannel ;
	
	SelectionKey selectKey;
	
	IoFilterChain ioFilterChain;
	
	private  IoSessionConfig sessionConfig = new SessionConfigImpl();
	
	private ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();	
	
	IoHandler ioHandler;
	
	public NioSocketSession(NioSocketAcceptor nioSocketAcceptor,
			NioProcessor processor, SocketChannel ch) {
		this.ioHandler = nioSocketAcceptor.getHandler();
		this.processor = processor;
		this.acceptor = nioSocketAcceptor;
		this.socketChannel = ch;
	}
	
	public NioProcessor getProcessor(){
		return this.processor;
	}
	
	@Override
	public SocketChannel getChannel() {
		return socketChannel;
	}
	
	
	@Override
	public void setSelectionKey(SelectionKey selectKey) {
		this.selectKey = selectKey;
	}
	
	public SelectionKey getSelectionKey(){
		return selectKey;
	}
	
	@Override
	public void setIoFilterChain(IoFilterChain ioFilterChain) {
		this.ioFilterChain = ioFilterChain;
	}
	
	public IoSessionConfig getSessionConfig(){
		return sessionConfig;
	}
	
	@Override
	public Object getAttribute(Object key){
		return attributes.get(key);
	}
	
	@Override
	public void setAttribute(Object key,Object value){
		attributes.put(key, value);
	}
	
	@Override
	public void setAttributeIfAbsent(Object key,Object value){
		  attributes.putIfAbsent(key, value);
	}
	
	@Override
	public void setIoHandler(IoHandler ioHandler) {
		this.ioHandler = ioHandler;
	}
	
	
	class SessionConfigImpl extends AbstractIoSessionConfig{
	}
	
	
	@Override
	public IoHandler getHandler() {
		return ioHandler;
	}

	@Override
	public IoFilterChain getIoFilterChain() {
		return ioFilterChain;
	}

	@Override
	public WriteFuture write(Object msg) {
		WriteFuture writeFuture = new DefaultWriteFuture();
		return writeFuture;
	}
}
