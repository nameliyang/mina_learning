package com.ly.sun.filter.codec;

import com.ly.sun.core.buffer.IoBuffer;
import com.ly.sun.core.session.IoSession;

public interface ProtocolDecoder {

	void decode (IoSession session,IoBuffer bin,ProtocolDecoderOutput out) throws Exception;
	
	
	
}
