package com.ly.sun.simplenioserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.ly.sun.simplenioserver.fiterchain.IoFilter;

public class SimpleAsynServer implements Runnable {
	
	int port;

	Selector selecor;

	boolean interrupt;

    IoProcessor ioProcessor;
	
	IoHandler ioHandler;
	
	public SimpleAsynServer(int processorCount) throws IOException {
		selecor = Selector.open();
		ioProcessor = new IoProcessor(processorCount);
	}
	
	public void setIoHandler(IoHandler ioHandler){
		this.ioHandler = ioHandler;
	}
	
	public SimpleAsynServer bind(int port) throws IOException {
		this.port = port;
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		serverSocketChannel.register(selecor, SelectionKey.OP_ACCEPT);
		return this;
	}

	@Override
	public void run() {
		while (!interrupt) {
			try{
				selecor.select();
				Set<SelectionKey> selectedKeys = selecor.selectedKeys();
				Iterator<SelectionKey> iterator = selectedKeys.iterator();
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					iterator.remove();
					if(!key.isValid()){
						continue;
					}
					
					if(key.isAcceptable()){
						try {
							ServerSocketChannel  serverSocketChannel = (ServerSocketChannel) key.channel();
							SocketChannel socketChannel = serverSocketChannel.accept();
							socketChannel.configureBlocking(false);
							NioSession session = new NioSession(ioProcessor,socketChannel);
							session.setIoHandler(ioHandler);
							session.getProcessor().addAcceptorSession(session);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
	
	public void start(){
		new Thread(this).start();
	}
	
	public static void main(String[] args) throws IOException {
		
		SimpleAsynServer server = new SimpleAsynServer(1);
		server.setIoHandler(new IoHandler());
		server.addIoFilter(null);
		server.bind(8080).start();
	}

	public  void addIoFilter(IoFilter ioFilter) {
		ioProcessor.getIoFilterChain().addFilter(ioFilter);
	}
}
