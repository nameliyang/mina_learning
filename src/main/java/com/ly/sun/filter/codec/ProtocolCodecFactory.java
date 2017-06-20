package com.ly.sun.filter.codec;

import com.ly.sun.core.session.IoSession;

/**
 * 解码器工厂类
 * @author liyang
 *
 */
public interface ProtocolCodecFactory {
	
	
	ProtocolEncoder getProtocolEncoder(IoSession session);
	
	
	ProtocolDecoder getProtocolDecoder(IoSession session);
	
}
