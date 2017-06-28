package com.ly.sun.core.session;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;

import com.ly.sun.core.filterchain.IoFilterChain;
import com.ly.sun.core.future.WriteFuture;
import com.ly.sun.core.service.IoHandler;
import com.ly.sun.core.write.WriteRequest;

public interface IoSession {
	
	
	Object getAttribute(Object key);
	
	void setAttribute(Object key,Object value);
	
	public void setAttributeIfAbsent(Object key,Object value);
	
	IoHandler getHandler();
	
	IoFilterChain getIoFilterChain();

	SocketChannel getChannel();

	void setSelectionKey(SelectionKey register);

	void setIoFilterChain(IoFilterChain ioFilterChain);

	void setIoHandler(IoHandler handler);
	 
	WriteFuture write(Object msg);
	
	Queue<WriteRequest> getWriteRequest();
	
}
