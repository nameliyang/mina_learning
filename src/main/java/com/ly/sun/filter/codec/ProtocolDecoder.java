package com.ly.sun.filter.codec;

import com.ly.sun.core.buffer.IoBuffer;
import com.ly.sun.transport.socket.nio.NioSocketSession;

public interface ProtocolDecoder {

	void decode (NioSocketSession session,IoBuffer bin,ProtocolDecoderOutput out) throws Exception;
	
	
	
}
