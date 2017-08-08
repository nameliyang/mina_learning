package com.ly.sun.simplenioserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import com.ly.sun.simplenioserver.fiterchain.DefaultIoFilterChain;
import com.ly.sun.simplenioserver.fiterchain.IoFilterChain;

public class IoProcessor {
	
	private static final ExecutorService servicePool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
	
	Selector selector;
	
	private  final Queue<NioSession> acceptorSessions = new ConcurrentLinkedQueue<NioSession>();
	
	private   final AtomicReference<Acceptor> accepotr = new AtomicReference<Acceptor>();
	
	private   final Queue<NioSession> flushSessions = new ConcurrentLinkedQueue<NioSession>();
	
	private static final IoFilterChain ioFilterChain = new DefaultIoFilterChain();

//	private static final int DEFAULT_IOPROCESSOR = 1;
	private IoProcessor[] ioProcessors ;
	
	private  IoProcessor() throws IOException {
		
	}
	
	public IoProcessor(int processorCount) throws IOException {
		ioProcessors   =  new IoProcessor[processorCount];
		for(int i = 0;i<processorCount;i++){
			ioProcessors[i] = new IoProcessor();
			ioProcessors[i].selector = Selector.open();
		}
	}
	
	public IoProcessor[] getIoProcessors(){
		return ioProcessors;
	}
	
	public IoFilterChain getIoFilterChain(){
		return  ioFilterChain;
	}
	
	
	public void process(NioSession session) throws IOException  {
		if(session.isReadable()){
			session.read();
		}
		if(session.isWriteable()){
			flushSessions.add(session);
		}
//		servicePool.submit(new Task(session));
	}

//	class Task implements Runnable{
//		
//		NioSession session;
//		
//		public Task(NioSession session) {
//			this.session = session;
//		}
//
//		@Override
//		public void run() {
//			try{
//				
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//		
//	}

	public void addAcceptorSession(NioSession session) {
		acceptorSessions.add(session);
		wakeup();
		if(accepotr.compareAndSet(null, new Acceptor())){
			servicePool.submit(accepotr.get());
		}
	}
	
	public void wakeup(){
		selector.wakeup();
	}
	
	public void addFlushSession(NioSession session){
		flushSessions.add(session);
	}
	
	class Acceptor implements Runnable{

		@Override
		public void run() {
			while(true){
				try {
					registerSelector();
					int select = selector.select();
//					Set<SelectionKey> keys = selector.keys();
//					SelectionKey test = keys.iterator().next();
//					System.out.println(test.interestOps() ==SelectionKey.OP_READ);
					if(select > 0){
						Set<SelectionKey> selectedKeys = selector.selectedKeys();
						Iterator<SelectionKey> iterator = selectedKeys.iterator();
						
						while(iterator.hasNext()){
							SelectionKey key = iterator.next();
							iterator.remove();
							NioSession nioSession = (NioSession) key.attachment();
							nioSession.getProcessor().process(nioSession);
						}
					}
					flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void flush() {
			for(NioSession session = flushSessions.poll();session!=null;session = flushSessions.poll()){
				flush(session);
			}
		}

		private void flush(NioSession session) {
			session.setFlushable(false);
			Queue<Object> messageQueue = session.getMessageQueue();
			for(Object writeMsg  = messageQueue.peek(); writeMsg!= null ; writeMsg = messageQueue.peek()){
				ByteBuffer writeBuffer = (ByteBuffer) writeMsg;
				try {
					session.registerWrite(false);
					session.getSocketChannel().write(writeBuffer);
					if(writeBuffer.hasRemaining()){
						session.registerWrite(true);
						return;
					}
					messageQueue.remove();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void registerSelector() {
			for (NioSession session = acceptorSessions.poll(); session != null; session = acceptorSessions
					.poll()) {
				session.registerReadEvent(selector);
				ioFilterChain.fireSessionCreated(session);
			}
		}
		
	}

	
}
