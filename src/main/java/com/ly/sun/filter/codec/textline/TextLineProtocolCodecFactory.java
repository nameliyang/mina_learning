package com.ly.sun.filter.codec.textline;

import com.ly.sun.core.session.IoSession;
import com.ly.sun.filter.codec.ProtocolCodecFactory;
import com.ly.sun.filter.codec.ProtocolDecoder;
import com.ly.sun.filter.codec.ProtocolEncoder;

public class TextLineProtocolCodecFactory implements ProtocolCodecFactory{

	@Override
	public ProtocolEncoder getProtocolEncoder(IoSession session) {
		return null;
	}

	
	@Override
	public ProtocolDecoder getProtocolDecoder(IoSession session) {
		return null;
	}

}
