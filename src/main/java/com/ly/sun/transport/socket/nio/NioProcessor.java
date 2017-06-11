package com.ly.sun.transport.socket.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import com.ly.sun.util.NamePerservingRunnable;

public class NioProcessor {
	
	private static final Queue<NioSocketSession> newSessions = new ConcurrentLinkedQueue<NioSocketSession>();
	
	private AtomicReference<Processor> processorRef = new AtomicReference<Processor>();
	
	private final Executor executor;
	
	Selector selector ;
	
	public NioProcessor(Executor executor) throws IOException{
		this(executor,Selector.open());
	}
	
	public NioProcessor(Executor executor,Selector selecor){
		if(executor ==  null){
			throw new IllegalArgumentException();
		}
		this.executor = executor;
	}
	
	public void add(NioSocketSession nioSession) {
		 newSessions.add(nioSession);
		 startupProcessor();
	}
	
	
	public void startupProcessor(){
		Processor processor = processorRef.get();
		if(processor == null){
			processor = new Processor();
			if(processorRef.compareAndSet(null, processor)){
				wakeup();
				executor.execute(new NamePerservingRunnable(processor, "ProcessorThread"));
			}
		}
	}
	
	public void wakeup(){
		selector.wakeup();
	}
	
	class Processor implements Runnable{
		
		@Override
		public void run() {
			for(;;){
				try {
					int select = selector.select();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	public int handlerNewSessioins(){
		int addedSessions = 0;
		for(NioSocketSession session = newSessions.poll();session!=null;session = newSessions.poll()){
			if(addNow(session)){
				addedSessions++;
			}
		}
		return addedSessions;
	}

	private boolean addNow(NioSocketSession session) {
		boolean registered = false;
		try{
			init(session);
		}catch(Exception e){
			
		}
		return false;
	}

	private void init(NioSocketSession session) throws IOException {
		SocketChannel channel = session.getChannel();
		channel.configureBlocking(false);
		session.setSelectionKey(channel.register(selector, SelectionKey.OP_READ,session));
	}
}
