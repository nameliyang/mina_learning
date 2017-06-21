package com.ly.sun.transport.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;

public class NioSocketAcceptorTest {
	public static void main(String[] args) throws IOException {
		
		NioSocketAcceptor acceptor = new NioSocketAcceptor();
	
		acceptor.bind(new InetSocketAddress(8080));
		
	}
}
