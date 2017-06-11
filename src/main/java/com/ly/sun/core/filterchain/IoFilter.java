package com.ly.sun.core.filterchain;

import com.ly.sun.transport.socket.nio.NioSocketSession;

public interface IoFilter {

	public void init() throws Exception;
	
	public void destory() throws Exception;
	
	public void sessionCreated(NextFilter nextFilter,NioSocketSession session);
	
	
	interface NextFilter{
		
		void sessionCreated(NioSocketSession sesson);
		
		
	}
}
