package com.ly.sun.filter.textline;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Queue;

import com.ly.sun.core.buffer.IoBuffer;
import com.ly.sun.core.filterchain.AbstractIoFilter;
import com.ly.sun.core.filterchain.DefaultIoFilterChain;
import com.ly.sun.core.filterchain.IoFilterChain;
import com.ly.sun.core.session.IoSession;
import com.ly.sun.filter.codec.AbstractProtocolDecoderOutput;
import com.ly.sun.filter.codec.textline.TextLineDecoder;
import com.ly.sun.transport.socket.nio.NioSocketSession;


public class TextLineDecoderTest {
	
	public static void main(String[] args) {
		 
		IoFilterChain ioFilterChain = new DefaultIoFilterChain(new NioSocketSession(null, null, null));
		ioFilterChain.addLast(new LoggerFilter("loggerFilter"));
		ioFilterChain.addLast(new TextLineFilter("textLineDecoder"));
		String str = "1234\r\nsdf\r\n12321312";
		
		ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
		IoBuffer  ioBuffer = IoBuffer.wrap(byteBuffer);
		ioFilterChain.fireMessageReceived(ioBuffer);
		ioFilterChain.fireMessageReceived(IoBuffer.wrap(ByteBuffer.wrap("456\r\n".getBytes())));
	}
}

class LoggerFilter extends AbstractIoFilter{
	
	public LoggerFilter(String name) {
		super(name);
	}
	
	@Override
	public void messageReceived(NextFilter nextFilter,
			IoSession session, Object msg) {
		System.out.println("loggerFilter start");
		nextFilter.messageReceived(session, msg);
		System.out.println("loggerFilter end");
	}
}
class TextLineFilter extends AbstractIoFilter{
	
	TextLineDecoder decoder = new TextLineDecoder(Charset.forName("UTF-8"));
	
	public TextLineFilter(String name) {
		super(name);
	}
	
	@Override
	public void messageReceived(NextFilter nextFilter,
			IoSession session, Object msg) {
		IoBuffer buffer = (IoBuffer) msg;
		
		AbstractProtocolDecoderOutput output = new AbstractProtocolDecoderOutput() {
		};
		try {
			decoder.decode(session, buffer, output);
			Queue<Object> messages = output.getMessages();
			String ioBuffer = null;
			while((ioBuffer = (String) messages.poll())!=null){
				System.out.println(ioBuffer);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void display(IoBuffer ioBuffer){
		ioBuffer.flip();
	}
}
