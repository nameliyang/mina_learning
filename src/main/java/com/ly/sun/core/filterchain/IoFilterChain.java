package com.ly.sun.core.filterchain;

import com.ly.sun.core.session.IoSession;

public interface IoFilterChain {
	
	IoSession getSession();
	
	void addFirst( IoFilter filter);
	
	void addLast( IoFilter filter);
	
	void fireSessionCreated();
	
	void fireMessageReceived(Object message);
	
}
