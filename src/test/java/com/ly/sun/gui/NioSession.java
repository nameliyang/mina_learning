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

	IoProcessor ioProcessor;
	
	private IoHandler ioHandler;
	
	private  final static int BUFFER_SIZE = Integer.parseInt(System.getProperty("BUFFER_SIZE", "1024"));
	
	ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
	
	ByteBuffer currentWriteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
	
	private Long sessionId ;
	
	private static final AtomicLong sessionGenerate = new AtomicLong();
	
	public NioSession(IoProcessor ioProcessor, SocketChannel socketChannel ) {
		this.ioProcessor = ioProcessor;
		this.socketChannel = socketChannel;
		sessionId = sessionGenerate.incrementAndGet(); 
	}

	public void registerReadEvent(Selector selecor)   {
		  try {
			selectionKey = socketChannel.register(selecor, SelectionKey.OP_READ,this);
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		}
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
	
	public void write(Object msg) throws IOException {
		if(msg  instanceof String) {
		}else if(msg instanceof ByteBuffer){
			ByteBuffer bufMsg = (ByteBuffer) msg;
			if(bufMsg.remaining() ==0 ){
				throw new RuntimeException("null msg");
			}
			int write = socketChannel.write(bufMsg);
			if(bufMsg.hasRemaining()){
				currentWriteBuffer = bufMsg.compact();
			}
		}
		throw new RuntimeException("unsupportDataType exception");
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
	
	public IoProcessor getProcessor(){
		return this.ioProcessor;
	}
	
}
