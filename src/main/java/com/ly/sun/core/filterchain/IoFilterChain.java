package com.ly.sun.core.filterchain;

import com.ly.sun.core.session.IoSession;
import com.ly.sun.core.write.WriteRequest;

public interface IoFilterChain {
	
	IoSession getSession();
	
	void addFirst( IoFilter filter);
	
	void addLast( IoFilter filter);
	
	void fireSessionCreated();
	
	void fireMessageReceived(Object message);

	void fireMessageWrite(WriteRequest writeRequest);
	
}
