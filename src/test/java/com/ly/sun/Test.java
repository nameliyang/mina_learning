package com.ly.sun;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.sun.core.service.IoHandlerAdapter;
import com.ly.sun.core.session.IoSession;

public class Test {
	public static void main(String[] args) throws IOException {
		
	
		
//		ByteBuffer byteBuffer = ByteBuffer.allocate(10);
//		byteBuffer.put((byte) '2');
//		byteBuffer.put((byte)'a');
//		byteBuffer.flip();// limit = position; posotion = 0;
//		
//		byteBuffer.compact(); //
		
//		char c = '李';
//		String binaryString = Integer.toBinaryString(c);
//		System.out.println(binaryString);
//		System.out.println(Integer.toHexString(c));
//		OutputStream os = new FileOutputStream("D:/test.txt");
//		os.write(c&0xff);
//		os.write(c>>8);
//		os.flush();
//		os.close();
		/*NioSocketAcceptor acceptor = new NioSocketAcceptor();
		acceptor.setHandler(new MyIoHandler());
		acceptor.bind(new InetSocketAddress(1234));*/
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



