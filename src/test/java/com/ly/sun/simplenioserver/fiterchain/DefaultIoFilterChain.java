package com.ly.sun.simplenioserver.fiterchain;

import java.io.IOException;
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
		//	System.out.println("header Filter before...");
			nextFilter.fireMessageReceived(session, byteBuffer);
		//	System.out.println("header filter after...");
		}
		
	}
	
    class TailerFilter extends AbstractIoFilter{

		public TailerFilter(String filterName) {
			super(filterName);
		}
		
    	@Override
    	public void fireMessageReceived(NextFilter nextFilter, NioSession session,ByteBuffer byteBuf) {
    		//System.out.println("tailer Filter before...");
    		try {
				session.getIoHandler().onReadData(session, byteBuf);
			} catch (IOException e) {
				e.printStackTrace();
			}
    		//System.out.println("tailer Filter after...");
    	}
    	
    	@Override
    	public void fireSessionCreated(NextFilter nextFilter, NioSession session) {
    		session.getIoHandler().sessionCreated(session);
    	}
    }

	public static void main(String[] args) {
		IoFilterChain ioFilterChain = new DefaultIoFilterChain();
	//	ioFilterChain.addFilter(new TestFilter("testFilterA"));
		
		ioFilterChain.addFilter(new ProtocolCodecFilter("ProtocolFilter"));
		
	//	ioFilterChain.addFilter(new TestFilter("testFilterB"));
		String msg = "hello12\r\n123";
		ByteBuffer buffer = ByteBuffer.allocate(100);
		buffer.put(msg.getBytes());
		buffer.flip();
		NioSession session = new NioSession(null, null);
		ioFilterChain.fireMessageReceived(session,buffer);
		buffer.compact();
		buffer.put((byte)'\r');
		buffer.put((byte)'\n');
		buffer.put("end\r\n".getBytes());
		buffer.flip();
		System.out.println(buffer);
		ioFilterChain.fireMessageReceived(session,buffer);
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


