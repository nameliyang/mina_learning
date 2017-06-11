package com.ly.sun.core.filterchain;

import com.ly.sun.transport.socket.nio.NioSocketSession;

public interface IoFilterChain {
	
	NioSocketSession getSession();
	
	void addFirst(String name,IoFilter filter);
	
	void addLast(String name,IoFilter filter);
	
	
	void fireSessionCreated();
	
	
	
}
