package com.ly.sun.filter.codec.textline;

import com.ly.sun.core.buffer.IoBuffer;
import com.ly.sun.filter.codec.ProtocolDecoder;
import com.ly.sun.filter.codec.ProtocolDecoderOutput;
import com.ly.sun.transport.socket.nio.NioSocketSession;

public class TextLineDecoder implements ProtocolDecoder{
	
	@Override
	public void decode(NioSocketSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		
		
	}
	
	
	public void decodeAuto(NioSocketSession session,IoBuffer in,ProtocolDecoderOutput out){
		int position = in.position();
		
		
	}
}	


