package com.ly.sun.transport.socket.nio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;

public final class NioSocketAcceptor {
	
	SelectorProvider selectorProvider = null;
	
	Selector selector ; 
	
	public ServerSocketChannel open(SocketAddress socketAddress) throws IOException{
		ServerSocketChannel serverSocketChannel = null;
		if(selectorProvider !=null){
			serverSocketChannel = selectorProvider.openServerSocketChannel();
		}else{
			serverSocketChannel = ServerSocketChannel.open();
		}
		serverSocketChannel.configureBlocking(false);
		ServerSocket socket = serverSocketChannel.socket();
		socket.setReuseAddress(true);
		
		serverSocketChannel.bind(socketAddress);
		return serverSocketChannel;
	}
}
