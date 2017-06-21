package com.ly.sun.filter.codec;

import com.ly.sun.core.filterchain.IoFilter.NextFilter;
import com.ly.sun.core.session.IoSession;

public interface ProtocolDecoderOutput {
	
	void write(Object msg);
	
	
	void flush(NextFilter nextFilter,IoSession session);
	
}
