package com.ly.sun.core.filterchain;

import com.ly.sun.transport.socket.nio.NioSocketSession;

public interface IoFilterChain {
	
	NioSocketSession getSession();
	
	void addFirst( IoFilter filter);
	
	void addLast( IoFilter filter);
	
	void fireSessionCreated();
	
	void fireMessageReceived(Object message);
	
}
