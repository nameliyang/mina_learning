package com.ly.sun.core.session;

import com.ly.sun.core.service.IoHandler;

public interface IoSession {
	
	
	Object getAttribute(Object key);
	
	void setAttribute(Object key,Object value);
	
	public void setAttributeIfAbsent(Object key,Object value);
	
	IoHandler getHandler();
}
