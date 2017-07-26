package com.ly.sun.gui;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SimpleAsynServer implements Runnable {

	int port;

	Selector selecor;

	boolean interrupt;

	IoProcessor ioProcessor;

	public SimpleAsynServer() throws IOException {
		selecor = Selector.open();
		ioProcessor = new IoProcessor(selecor);
	}

	public SimpleAsynServer bind(int port) throws IOException {
		this.port = port;
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.bind(new InetSocketAddress(port));
		serverSocketChannel.register(selecor, SelectionKey.OP_ACCEPT);
		return this;
	}

	@Override
	public void run() {
		while (interrupt) {
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
						NioSession session = new NioSession(socketChannel,key);
						session.registerReadEvent(selecor);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					NioSession session = (NioSession) key.attachment();
					ioProcessor.process(session);
				}
				
			}
		}

	}

	public static void main(String[] args) {
	}
}
