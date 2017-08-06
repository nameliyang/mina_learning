package com.ly.sun.simplenioserver.fiterchain;

import java.nio.ByteBuffer;

import com.ly.sun.simplenioserver.NioSession;

public interface IoFilterChain {
	
	public IoFilterChain addFilter(IoFilter filter);
	

	void fireMessageReceived(NioSession session,ByteBuffer buffer);
	
	void fireSessionCreated(NioSession session);
}
