package com.ly.sun.simplenioserver.fiterchain;

import java.nio.ByteBuffer;

import com.ly.sun.simplenioserver.NioSession;

public abstract class IoFilter {

	IoFilter nextFilter;

	IoFilter preFilter;
	
	
	NextFilter nextIoFilter = new NextFilter() {
		
		@Override
		public void fireSessionCreated(NioSession session) {
			if(nextFilter != null){
				NextFilter nextIoFilter = nextFilter.nextIoFilter;
				nextFilter.fireSessionCreated(nextIoFilter, session);
			}
			
		}
		
		@Override
		public void fireMessageReceived(NioSession session, ByteBuffer byteBuffer) {
			if(nextFilter!= null){
				NextFilter nextIoFilter = nextFilter.nextIoFilter;
				nextFilter.fireMessageReceived(nextIoFilter, session, byteBuffer);
			}
		}
	};
	
	
	public abstract void fireMessageReceived(NextFilter nextFilter,
			NioSession session, ByteBuffer byteBuffer);

	public abstract void fireSessionCreated(NextFilter nextFilter,
			NioSession session);

}
