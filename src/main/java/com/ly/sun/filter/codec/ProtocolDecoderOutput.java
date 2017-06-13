package com.ly.sun.filter.codec;

import com.ly.sun.core.filterchain.IoFilter.NextFilter;
import com.ly.sun.transport.socket.nio.NioSocketSession;

public interface ProtocolDecoderOutput {
	
	void write(Object msg);
	
	
	void flush(NextFilter nextFilter,NioSocketSession session);
	
}
