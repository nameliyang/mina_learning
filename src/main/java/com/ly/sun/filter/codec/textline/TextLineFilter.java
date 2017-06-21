package com.ly.sun.filter.codec.textline;

import java.nio.charset.Charset;
import java.util.Queue;

import com.ly.sun.core.buffer.IoBuffer;
import com.ly.sun.core.filterchain.AbstractIoFilter;
import com.ly.sun.core.session.IoSession;
import com.ly.sun.filter.codec.AbstractProtocolDecoderOutput;

public class TextLineFilter extends AbstractIoFilter{
	
	TextLineDecoder decoder = new TextLineDecoder();
	
	public TextLineFilter(String name,Charset charset) {
		super(name);
		decoder = new TextLineDecoder(charset);
	}
	
	public  TextLineFilter(String name) {
		this(name, Charset.defaultCharset());
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