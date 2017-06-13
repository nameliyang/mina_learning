package com.ly.sun.filter.codec;

import com.ly.sun.transport.socket.nio.NioSocketSession;

/**
 * 解码器工厂类
 * @author liyang
 *
 */
public interface ProtocolCodecFactory {
	
	
	ProtocolEncoder getProtocolEncoder(NioSocketSession session	);
	
	
	ProtocolDecoder getProtocolDecoder(NioSocketSession session);
	
}
