package com.ly.sun.simplenioserver.fiterchain;

import java.nio.ByteBuffer;

import com.ly.sun.simplenioserver.NioSession;

public class DefaultIoFilterChain implements IoFilterChain {
	
	private IoFilter headFilter ;
	
	private IoFilter tailFilter;
	
	private IoFilter currentFilter;
	
	
	public   DefaultIoFilterChain() {
		 headFilter = new HeaderFilter("header");
		 tailFilter = new TailerFilter("tailer");
		 headFilter.nextFilter = tailFilter;
		 tailFilter.preFilter = headFilter;
	}
	
	@Override
	public IoFilterChain addFilter(IoFilter filter) {
		IoFilter preFilter = tailFilter.preFilter;
		filter.nextFilter = tailFilter;
		tailFilter.preFilter = filter;
		preFilter.nextFilter = filter;
		filter.preFilter = preFilter;
		return this;
	}

	
//	@Override
	public void doNextFilter(NioSession session) {
		IoFilter nextFilter = currentFilter.nextFilter;
		currentFilter = nextFilter;
		
	}
	
	@Override
	public void fireMessageReceived(NioSession session,ByteBuffer byteBuffer) {
		NextFilter nextFilter = headFilter.nextIoFilter;
		headFilter.fireMessageReceived(nextFilter, session, byteBuffer);
	}

	@Override
	public void fireSessionCreated(NioSession session) {
		NextFilter nextFilter = headFilter.nextIoFilter;
		headFilter.fireSessionCreated(nextFilter, session);
	} 
	
	
	
    class HeaderFilter extends AbstractIoFilter{

		public HeaderFilter(String filterName) {
			super(filterName);
		}
		
		@Override
		public void fireMessageReceived(NextFilter nextFilter, NioSession session,ByteBuffer byteBuffer) {
			System.out.println("header Filter before...");
			nextFilter.fireMessageReceived(session, byteBuffer);
			System.out.println("header filter after...");
		}
		
	}
	
    class TailerFilter extends AbstractIoFilter{

		public TailerFilter(String filterName) {
			super(filterName);
		}
		
    	@Override
    	public void fireMessageReceived(NextFilter nextFilter, NioSession session,ByteBuffer byteBuf) {
    		System.out.println("tailer Filter before...");
    		System.out.println("tailer Filter after...");
    	}
    	
    	@Override
    	public void fireSessionCreated(NextFilter nextFilter, NioSession session) {
    		session.getIoHandler().sessionCreated(session);
    	}
    }

	public static void main(String[] args) {
		IoFilterChain ioFilterChain = new DefaultIoFilterChain();
		ioFilterChain.addFilter(new TestFilter("testFilterA"));
		
		ioFilterChain.addFilter(new ProtocolCodecFilter("ProtocolFilter"));
		
	//	ioFilterChain.addFilter(new TestFilter("testFilterB"));
		String msg = "hello\r12\r\n123";
		
		ioFilterChain.fireMessageReceived(null,ByteBuffer.wrap(msg.getBytes()));		
		ioFilterChain.fireMessageReceived(null,ByteBuffer.wrap("\r\n".getBytes()));
	}
	
	static class TestFilter extends AbstractIoFilter{

		public TestFilter(String filterName) {
			super(filterName);
		}
		
		@Override
		public void fireMessageReceived(NextFilter nextFilter, NioSession session,ByteBuffer buf) {
			System.out.println(filterName+" test filter before...");
			nextFilter.fireMessageReceived(session, buf);
			System.out.println(filterName+" test filter after...");
		}
	}

	
}


