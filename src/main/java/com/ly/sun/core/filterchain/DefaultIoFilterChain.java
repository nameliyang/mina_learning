package com.ly.sun.core.filterchain;

import com.ly.sun.core.filterchain.IoFilter.NextFilter;
import com.ly.sun.transport.socket.nio.NioSocketSession;

public class DefaultIoFilterChain implements IoFilterChain {
	
	private NioSocketSession session;
	
	private IoFilter header ;
	
	private IoFilter tailer ;
	
	
	public   DefaultIoFilterChain(NioSocketSession session) {
		this.session = session;
		header = new HeaderFilter();
		tailer = new TailerFilter();
		
		header.nextIoFilter = tailer;
		tailer.preIoFilter = header;
	}
	
	@Override
	public NioSocketSession getSession() {
		return session;
	}

	@Override
	public void addFirst(  IoFilter filter) {
		
	}

	@Override
	public void addLast(  IoFilter filter) {
		IoFilter preIoFilter = tailer.preIoFilter;
		preIoFilter.nextIoFilter = filter;
		filter.preIoFilter = preIoFilter;
		
		filter.nextIoFilter = tailer;
		tailer.preIoFilter = filter;
	}

	@Override
	public void fireSessionCreated() {
		NextFilter nextFilter = header.nextFilter;
		header.sessionCreated(nextFilter, session);
	}
	
	class HeaderFilter extends AbstractIoFilter{
		public HeaderFilter() {
			super("headFilter");
		}
		@Override
		public void sessionCreated(NextFilter nextFilter, NioSocketSession session) {
			System.out.println("header filter...before");
			nextFilter.sessionCreated(session);
			System.out.println("header filter...after");
		}
	}
	
	class TailerFilter extends AbstractIoFilter{
		
		public TailerFilter(){
			super("tailFilter");
		}
		@Override
		public void sessionCreated(NextFilter nextFilter, NioSocketSession session) {
			System.out.println("session created...");
		}
	}
	
}