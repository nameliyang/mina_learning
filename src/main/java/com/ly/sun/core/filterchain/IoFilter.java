package com.ly.sun.core.filterchain;

import com.ly.sun.core.session.IoSession;

public abstract  class  IoFilter {
	
    IoFilter  preIoFilter;
    
	IoFilter  nextIoFilter;
	
	String name;
	
	public IoFilter(String name){
		this.name = name;
	}
	
	NextFilter nextFilter = new NextFilter() {
		@Override
		public void sessionCreated(IoSession session) {
			IoFilter nextIoFilter = IoFilter.this.nextIoFilter;
			NextFilter nextFilter = IoFilter.this.nextIoFilter.nextFilter;
			nextIoFilter.sessionCreated(nextFilter, session);
		}

		@Override
		public void messageReceived(IoSession session, Object msg) {
			IoFilter nextIoFilter = IoFilter.this.nextIoFilter;
			NextFilter nextFilter = IoFilter.this.nextIoFilter.nextFilter;
			nextIoFilter.messageReceived(nextFilter, session,msg);
		}
	};
	
	public  abstract void init() throws Exception;
	
	public abstract  void destory() throws Exception;
	
	public abstract  void sessionCreated(NextFilter nextFilter,IoSession session);
	
	public abstract  void messageReceived(NextFilter nextFilter,IoSession session,Object msg);
	
	public interface NextFilter{
		
		void sessionCreated(IoSession session);
		
		void messageReceived(IoSession session,Object msg);
	}

}
