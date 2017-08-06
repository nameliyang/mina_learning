package com.ly.sun.simplenioserver.fiterchain;

import java.nio.ByteBuffer;

import com.ly.sun.simplenioserver.NioSession;

public abstract class AbstractIoFilter extends IoFilter {
	
	String filterName;
	
	public AbstractIoFilter(String filterName) {
		this.filterName = filterName;
	}
	
	@Override
	public void fireMessageReceived(NextFilter nextFilter, NioSession session,ByteBuffer byteBuffer) {
		nextFilter.fireMessageReceived(session, byteBuffer);
	}

	@Override
	public void fireSessionCreated(NextFilter nextFilter, NioSession session) {
		nextFilter.fireSessionCreated(session);
	}

	@Override
	public String toString() {
		return "filterName:"+filterName;
	}
	
	
}
