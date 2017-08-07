package com.ly.sun.simplenioserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
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
	
	private Queue<Object> writeMsgs = new ConcurrentLinkedQueue<Object>();
	
	private static final AtomicBoolean scheduleForflush = new AtomicBoolean();
	
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
		//	ioHandler.onReadData(this,buffer);
			this.getProcessor().getIoFilterChain().fireMessageReceived(this,buffer);
			buffer.compact();
		}
	}
	
	public void registerWrite(boolean regiserWrite){
		if(regiserWrite){
			//注册write事件
			selectionKey.interestOps(selectionKey.interestOps()|SelectionKey.OP_WRITE);
		}else{
			//取
			selectionKey.interestOps(selectionKey.interestOps()& ~SelectionKey.OP_WRITE);
		}
	}
	
	public void setFlushable(boolean flushable){
		scheduleForflush.set(flushable);
	}
	
	public void write(Object msg) throws IOException {
		if(msg  instanceof String) {
		}else if(msg instanceof ByteBuffer){
			writeMsgs.add(msg);
			if(scheduleForflush.compareAndSet(false, true)){
				this.getProcessor().addFlushSession(this);
			}
//			ByteBuffer bufMsg = (ByteBuffer) msg;
//			if(bufMsg.remaining() ==0 ){
//				throw new RuntimeException("null msg");
//			}
//			int write = socketChannel.write(bufMsg);
//			if(bufMsg.hasRemaining()){
//				currentWriteBuffer = bufMsg.compact();
//			}
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
	
	public IoProcessor getProcessor(){
		Long sessionId = getSessionId();
		IoProcessor[] ioProcessors = ioProcessor.getIoProcessors();
		IoProcessor ioProcessor = ioProcessors[(int)(sessionId%ioProcessors.length)];
		return ioProcessor;
	}

	public Queue<Object> getMessageQueue() {
		return writeMsgs;
	}
	
	
	public SocketChannel getSocketChannel(){
		return this.socketChannel;
	}
	
	public IoHandler getIoHandler(){
		return ioHandler;
	}
}
