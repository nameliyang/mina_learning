package com.ly.sun.core.filterchain;

import com.ly.sun.core.session.IoSession;
import com.ly.sun.core.write.WriteRequest;

public class AbstractIoFilter extends IoFilter{
	
	public AbstractIoFilter(String name){
		super(name);
	}
	
	@Override
	public void init() throws Exception {
	}

	@Override
	public void destory() throws Exception {
	}
	
	@Override
	public void sessionCreated(NextFilter nextFilter, IoSession session) {
		nextFilter.sessionCreated(session);
	}
	
	
	@Override
	public void messageReceived(NextFilter nextFilter,
			IoSession session, Object msg) {
		nextFilter.messageReceived(session, msg);
	}
	
	@Override
	public String toString() {
		return "Filter:"+name;
	}

	@Override
	public void messageWrite(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) {
		nextFilter.messageWrite(session, writeRequest);
	}

}
