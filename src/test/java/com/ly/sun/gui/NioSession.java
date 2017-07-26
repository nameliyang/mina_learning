package com.ly.sun.gui;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicLong;

public class NioSession {

	SocketChannel socketChannel;
	
	SelectionKey selectionKey;


	private IoHandler ioHandler;
	
	private  final static int BUFFER_SIZE = Integer.parseInt(System.getProperty("BUFFER_SIZE", "1024"));
	
	ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
	
	private Long sessionId ;
	
	private static final AtomicLong sessionGenerate = new AtomicLong();
	
	public NioSession(SocketChannel socketChannel, SelectionKey key) {
		this.socketChannel = socketChannel;
		this.selectionKey = key;
		sessionId = sessionGenerate.incrementAndGet(); 
	}

	public void registerReadEvent(Selector selecor) throws ClosedChannelException {
		socketChannel.register(selecor, SelectionKey.OP_READ,this);
	}

	public boolean isReadable() {
		return selectionKey.isValid()&&selectionKey.isReadable();
	}
	
	public boolean isWriteable() {
		return selectionKey.isValid()&&selectionKey.isWritable();
	}

	public void read() throws IOException {
		int read  = socketChannel.read(buffer);
		if(read > 0){
			buffer.flip();
			ioHandler.onReadData(this,buffer);
		}
	}
	
	public void readBufferRelese(){
		buffer.clear();
	}

	public void setIoHandler(IoHandler ioHandler) {
		this.ioHandler = ioHandler;
	}

	public Long getSessionId() {
		return sessionId;
	}
	
}
