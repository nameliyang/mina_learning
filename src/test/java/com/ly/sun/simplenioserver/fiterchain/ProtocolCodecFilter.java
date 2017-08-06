package com.ly.sun.simplenioserver.fiterchain;

import java.nio.ByteBuffer;

import com.ly.sun.simplenioserver.NioSession;

public class ProtocolCodecFilter extends AbstractIoFilter{

	public ProtocolCodecFilter(String filterName) {
		super(filterName);
	}
	
	@Override
	public void fireMessageReceived(NextFilter nextFilter, NioSession session,
			ByteBuffer byteBuffer) {
		
		
		
	}
	
	
}


