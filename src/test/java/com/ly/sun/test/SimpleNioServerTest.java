package com.ly.sun.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.sun.core.buffer.IoBuffer;

public class SimpleNioServerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleNioServerTest.class);
	
	public static void main(String[] args) throws IOException {
		
		SelectServer selectServer = new SelectServer();
		
		selectServer.setIoHandler(new SelectServer.IoHandler() {
			
			@Override
			public void messageSend(SelectServer.MyIoSession session,Object messge) {
				
			}
			
			@Override
			public void messageReceived(SelectServer.MyIoSession  session,Object message) {
				IoBuffer buffer = (IoBuffer) message;
				buffer.flip();
				StringBuilder sb = new StringBuilder();
				while(buffer.hasRemaining()){
					sb.append((char)buffer.get());
				}
				String readMessage = sb.toString();
				if("exit".equals(readMessage)){
					try {
						session.close();
					} catch (IOException e) {
						logger.error("",e);
					}
				}
				logger.info("read msg={}",readMessage);
				String rtnMsg = sb.toString().toUpperCase();
				session.dowrite(rtnMsg);
				
			}
		});
		
		selectServer.bind(23);
		
		selectServer.start();
	}
}

class SelectServer{
	
	private static final Logger logger = LoggerFactory.getLogger(SelectServer.class);
	
	private   Selector selector ;
	
	private volatile boolean interrupted = false;
	private IoHandler ioHandler ;
	
	public SelectServer(){
		try {
			selector = Selector.open();
		} catch (IOException e) {
			logger.error("实例化selector出错",e);
		}
	}
	
	public void setIoHandler(IoHandler ioHandler){
		this.ioHandler = ioHandler;
	}
	
	public void bind(int port) throws IOException{
		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.socket().bind(new InetSocketAddress(port));
		channel.socket().setReuseAddress(true);
		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_ACCEPT);
	}
	
	
	public void start(){
		new Thread(new Acceptor()).start();
	}
	
	class Acceptor implements Runnable{
		
		
		@Override
		public void run() {
			while(!interrupted){
				try {
					int select = selector.select();
					if(select > 0 ){
						Set<SelectionKey> selectedKeys = selector.selectedKeys();
						Iterator<SelectionKey> iterator = selectedKeys.iterator();
						while(iterator.hasNext()){
							SelectionKey key = iterator.next();
							iterator.remove();
							handler(key);
						}
					}
					
				} catch (IOException e) {
					logger.error("",e);
				}
			}
		}

		private void handler(SelectionKey key) throws IOException {
			if(key.isAcceptable()){
				ServerSocketChannel serverSocketchannel = (ServerSocketChannel) key.channel();
				SocketChannel socketChannel = serverSocketchannel.accept();
				socketChannel.configureBlocking(false);
				socketChannel.register(selector, SelectionKey.OP_READ,new MyIoSession(key,socketChannel));
			}else if(key.isReadable()){
				SocketChannel socketChannel = (SocketChannel) key.channel();
				MyIoSession session = getSession(key);
				session.doread();
			}else if(key.isWritable()){
				MyIoSession session = getSession(key);
				session.flush();
			}
		}
		
		public MyIoSession getSession(SelectionKey key){
			MyIoSession session = (MyIoSession) key.attachment();
			return session;
		}
	}
	
	  class MyIoSession{
		
		private SelectionKey key;
		
		IoBuffer ioBuffer = IoBuffer.allocate(20);
		
		private SocketChannel socketChannel;
		
		Queue<IoBuffer> queue = new ConcurrentLinkedQueue<IoBuffer>();
		
		IoBuffer writeBuffer = IoBuffer.allocate(20).setAutoExpand(true);
		
		int match ;
		
		volatile boolean scheduledWrite = false;
		
		public MyIoSession(SelectionKey key,SocketChannel socketChannel) {
			this.key = key;
			this.socketChannel = socketChannel;
		}


		public void close() throws IOException {
			key.cancel();
			socketChannel.close();
		}


		/*public void flush(Acceptor acceptor) throws IOException {
			 socketChannel.write(writeBuffer.buf());
			 writeBuffer.compact();
			 if(writeBuffer.hasRemaining()){
				 this.scheduledWrite  = true;
			 }
		}*/


		public void flush() throws IOException {
			SocketChannel socketChannel = this.socketChannel;
			socketChannel.write(writeBuffer.buf());
			if(!writeBuffer.hasRemaining()){
				key.interestOps(key.interestOps()&~SelectionKey.OP_WRITE);
			}
			
		}


		public void dowrite(Object message) {
			if(message instanceof String){
				IoBuffer buffer = IoBuffer.wrap(ByteBuffer.wrap(((String) message).getBytes()));
				writeBuffer.put(buffer);
				scheduledWrite = true;
			}
		}

		public void doread() throws IOException {
			int readCount = 0;
			int read;
			
			while((read= readBuffer()) > 0){
				readCount += read;
				if(!ioBuffer.hasRemaining()){
					break;
				}
			}
			
			if(readCount > 0 ){
				ioBuffer.flip();
				int oldlimit = ioBuffer.limit();
				int oldPos = ioBuffer.position();
				while(ioBuffer.hasRemaining()){
					byte b = ioBuffer.get();
					switch(b){
					case '\r':
						match++;
						break;
					case '\n':
						match++;
						int pos = ioBuffer.position();
						ioBuffer.position(oldPos);
						ioBuffer.limit(oldlimit-match);
						this.append(ioBuffer);
						ioBuffer.limit(oldlimit);
						ioBuffer.position(pos);
						oldPos = pos;
						match=0;
						break;
					default:
						match = 0;
						;
					}
				}
				ioBuffer.position(oldPos);
				ioBuffer.limit(oldlimit);
				ioBuffer.compact();
				if(!ioBuffer.hasRemaining()){
					ioBuffer.expand(ioBuffer.capacity());
				}
				for(IoBuffer ioBuffer = queue.poll();ioBuffer!=null;ioBuffer = queue.poll()){
					ioHandler.messageReceived(this,ioBuffer);
				}
			}
			
			if(scheduledWrite){
				 writeBuffer.flip();
				 socketChannel.write(writeBuffer.buf());
				 if(writeBuffer.hasRemaining()){
					 this.scheduledWrite  = true;
					 socketChannel.register(selector, SelectionKey.OP_WRITE);
				 }else{
					 scheduledWrite = false;
				 }
				 writeBuffer.compact();
			}
			
			
		}
		
		
		private void append(IoBuffer ioBuffer) {
			IoBuffer buffer = IoBuffer.allocate(ioBuffer.remaining());
			buffer.put(ioBuffer);
			queue.add(buffer);
		}

		public int readBuffer() throws IOException{
			int read = socketChannel.read(ioBuffer.buf());
			return read;
		}
		
	}
	
	interface IoHandler{
		
		public   void messageReceived(MyIoSession session,Object message);
		
		public   void messageSend(MyIoSession session,Object messge);
	}
	
	class MyIoFilterChain{
		
		private IoFilter ioFilter;
		
		abstract class IoFilter{
			
			IoFilter preIoFilter;
			  
			IoFilter nextIoFilter;
			
			public abstract void messageReceived(NextFilter nextFilter,Object message);
			
			public abstract void messageSend(NextFilter nextFilter ,Object messge);
			
		}
		
		
		class NextFilter  {

			public void messageReceived(NextFilter nextFilter, Object message) {
			}

			public void messageSend(NextFilter nextFilter, Object messge) {
			}
		}
		
		
		
		
	}
}

