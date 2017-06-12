package com.ly.sun.core.filterchain;

import com.ly.sun.transport.socket.nio.NioSocketSession;

public abstract  class  IoFilter {
	
    IoFilter  preIoFilter;
    
	IoFilter  nextIoFilter;
	
	String name;
	
	public IoFilter(String name){
		this.name = name;
	}
	
	NextFilter nextFilter = new NextFilter() {
		@Override
		public void sessionCreated(NioSocketSession session) {
			IoFilter nextIoFilter = IoFilter.this.nextIoFilter;
			NextFilter nextFilter = IoFilter.this.nextIoFilter.nextFilter;
			nextIoFilter.sessionCreated(nextFilter, session);
		}

		@Override
		public void messageReceived(NioSocketSession session, Object msg) {
			IoFilter nextIoFilter = IoFilter.this.nextIoFilter;
			NextFilter nextFilter = IoFilter.this.nextIoFilter.nextFilter;
			nextIoFilter.messageReceived(nextFilter, session,msg);
		}
	};
	
	public  abstract void init() throws Exception;
	
	public abstract  void destory() throws Exception;
	
	public abstract  void sessionCreated(NextFilter nextFilter,NioSocketSession session);
	
	public abstract  void messageReceived(NextFilter nextFilter,NioSocketSession session,Object msg);
	
	interface NextFilter{
		
		void sessionCreated(NioSocketSession session);
		
		void messageReceived(NioSocketSession session,Object msg);
	}
}
