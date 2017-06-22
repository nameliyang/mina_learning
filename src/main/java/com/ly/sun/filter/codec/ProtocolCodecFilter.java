package com.ly.sun.filter.codec;

import com.ly.sun.core.buffer.IoBuffer;
import com.ly.sun.core.filterchain.AbstractIoFilter;
import com.ly.sun.core.filterchain.IoFilter.NextFilter;
import com.ly.sun.core.session.IoSession;

public class ProtocolCodecFilter extends AbstractIoFilter{
	
	ProtocolCodecFactory factory;
	
	public ProtocolCodecFilter(String name,ProtocolCodecFactory factory) {
		super(name);
		this.factory = factory;
	}
	
//	@Override
//	public void sessionCreated(NextFilter nextFilter, IoSession session) {
//		super.sessionCreated(nextFilter, session);
//	}
//	
	
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object msg) {
		ProtocolDecoder decoder = factory.getProtocolDecoder(session);
		ProtocolDecoderOutput out = getDecoderOutput(session);
		try {
			decoder.decode(session, (IoBuffer) msg, out);
			out.flush(nextFilter, session);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ProtocolDecoderOutput getDecoderOutput(IoSession session) {
		
		return new AbstractProtocolDecoderOutput() {
		};
			
	}
}
