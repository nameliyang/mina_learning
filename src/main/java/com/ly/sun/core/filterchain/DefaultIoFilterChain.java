package com.ly.sun.core.filterchain;

import com.ly.sun.core.filterchain.IoFilter.NextFilter;
import com.ly.sun.core.service.IoHandler;
import com.ly.sun.core.session.IoSession;
import com.ly.sun.core.write.WriteRequest;

public class DefaultIoFilterChain implements IoFilterChain {
	
	private IoSession session;
	
	private IoFilter header ;
	
	private IoFilter tailer ;
	
	public   DefaultIoFilterChain(IoSession session) {
		this.session = session;
		header = new HeaderFilter();
		tailer = new TailerFilter();
		
		header.nextIoFilter = tailer;
		tailer.preIoFilter = header;
	}
	
	@Override
	public IoSession getSession() {
		return session;
	}

	@Override
	public void addFirst(IoFilter filter) {
		
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
	
	@Override
	public void fireMessageWrite(WriteRequest writeRequest) {
		NextFilter nextFilter = tailer.nextFilter;
		header.sessionCreated(nextFilter, session);
	}

	@Override
	public void fireMessageReceived(Object message) {
		NextFilter nextFilter = header.nextFilter;
		header.messageReceived(nextFilter,session,message);
	}
	
	class HeaderFilter extends AbstractIoFilter{
		public HeaderFilter() {
			super("headFilter");
		}
		@Override
		public void sessionCreated(NextFilter nextFilter, IoSession session) {
			System.out.println("header filter...before");
			nextFilter.sessionCreated(session);
			System.out.println("header filter...after");
		}
		@Override
		public void messageWrite(NextFilter nextFilter, IoSession session,
				WriteRequest writeRequest) {
			Object message = writeRequest.getMessage();
			if(message  instanceof String){
				session.getWriteRequest().offer(writeRequest);
			}
			throw new RuntimeException("");
		}
	}
	
	class TailerFilter extends AbstractIoFilter{
		
		public TailerFilter(){
			super("tailFilter");
		}
		
		@Override
		public void sessionCreated(NextFilter nextFilter, IoSession session) {
			System.out.println("session created...");
		}
		
		@Override
		public void messageReceived(NextFilter nextFilter, IoSession session,
				Object msg) {
			IoHandler handler = session.getHandler();
			try {
				handler.messageReceived(session, msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	
}
