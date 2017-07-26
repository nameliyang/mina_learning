package com.ly.sun.gui;

import java.nio.channels.Selector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IoProcessor {
	
	ExecutorService servicePool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
	
	Selector selecor;
	
	public IoProcessor(Selector selecor) {
		this.selecor = selecor;
	}

	public void process(NioSession session) {
		servicePool.submit(new Task(session));
		
	}

	class Task implements Runnable{
		
		NioSession session;
		
		public Task(NioSession session) {
			this.session = session;
		}

		@Override
		public void run() {
			try{
				if(session.isReadable()){
					session.read();
				}
				
				if(session.isWriteable()){
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

}
