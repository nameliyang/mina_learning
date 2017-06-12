package com.ly.sun.core.filterchain;

import com.ly.sun.transport.socket.nio.NioSocketSession;

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
	public void sessionCreated(NextFilter nextFilter, NioSocketSession session) {
		nextFilter.sessionCreated(session);
	}
	
	@Override
	public String toString() {
		return "Filter:"+name;
	}
}
