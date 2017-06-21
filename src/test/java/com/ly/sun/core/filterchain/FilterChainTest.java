package com.ly.sun.core.filterchain;

import java.nio.channels.SocketChannel;

import com.ly.sun.core.session.IoSession;
import com.ly.sun.transport.socket.nio.NioProcessor;
import com.ly.sun.transport.socket.nio.NioSocketAcceptor;
import com.ly.sun.transport.socket.nio.NioSocketSession;

public class FilterChainTest {
	
	public static void main(String[] args) {
		
		DummySession session = new DummySession(null,null,null);
		
		DefaultIoFilterChain chain = new DefaultIoFilterChain(session);
		
		chain.addLast(new AbstractIoFilter("loggerA"){
			@Override
			public void sessionCreated(NextFilter nextFilter, IoSession session) {
				System.out.println("loggerA filter before...");
				nextFilter.sessionCreated(session);
				System.out.println("loggerA filter after...");
			}
		});
		
		chain.addLast(new AbstractIoFilter("loggerB"){
			@Override
			public void sessionCreated(NextFilter nextFilter, IoSession session) {
				System.out.println("loggerB filter before...");
				nextFilter.sessionCreated(session);
				System.out.println("loggerB filter after...");
			}
		});
		
		chain.addLast(new AbstractIoFilter("loggerC"){
			@Override
			public void sessionCreated(NextFilter nextFilter, IoSession session) {
				System.out.println("loggerC filter before...");
				nextFilter.sessionCreated(session);
				System.out.println("loggerC filter after...");
			}
		});
		chain.fireSessionCreated();
	}
	
	static class DummySession extends NioSocketSession{

		public DummySession(NioSocketAcceptor nioSocketAcceptor, NioProcessor processor, SocketChannel ch) {
			super(nioSocketAcceptor, processor, ch);
		}
		
	}
}
