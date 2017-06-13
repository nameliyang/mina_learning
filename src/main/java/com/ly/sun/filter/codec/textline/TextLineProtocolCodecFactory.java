package com.ly.sun.filter.codec.textline;

import com.ly.sun.filter.codec.ProtocolCodecFactory;
import com.ly.sun.filter.codec.ProtocolDecoder;
import com.ly.sun.filter.codec.ProtocolEncoder;
import com.ly.sun.transport.socket.nio.NioSocketSession;

public class TextLineProtocolCodecFactory implements ProtocolCodecFactory{

	@Override
	public ProtocolEncoder getProtocolEncoder(NioSocketSession session) {
		return null;
	}

	
	@Override
	public ProtocolDecoder getProtocolDecoder(NioSocketSession session) {
		return null;
	}

}
