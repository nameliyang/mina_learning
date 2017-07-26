package com.ly.sun.gui;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class NioSession {

	SocketChannel socketChannel;
	
	SelectionKey selectionKey;
	
	
	public NioSession(SocketChannel socketChannel, SelectionKey key) {
		this.socketChannel = socketChannel;
		this.selectionKey = key;
	}

	public void registerReadEvent(Selector selecor) throws ClosedChannelException {
		socketChannel.register(selecor, SelectionKey.OP_READ,this);
	}

	public boolean isReadable() {
		return false;
	}
	
	public boolean isWriteable() {
		return false;
	}
	
}
