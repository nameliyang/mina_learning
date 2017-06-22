package com.ly.sun;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.sun.core.service.IoHandlerAdapter;
import com.ly.sun.core.session.IoSession;
import com.ly.sun.transport.socket.nio.NioSocketAcceptor;

public class Test {
	public static void main(String[] args) throws IOException {
		NioSocketAcceptor acceptor = new NioSocketAcceptor();
		acceptor.setHandler(new MyIoHandler());
		acceptor.bind(new InetSocketAddress(1234));
	}
	
}
class MyIoHandler extends IoHandlerAdapter{
	
	Logger logger = LoggerFactory.getLogger(MyIoHandler.class);
	
	public MyIoHandler() {
	}
	@Override
	public void messageReceived(IoSession session, Object msg) throws Exception {
		logger.info("message received ..."+msg);
	}
	
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("session created ......");
	}
}



