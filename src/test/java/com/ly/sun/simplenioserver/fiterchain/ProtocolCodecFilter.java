package com.ly.sun.simplenioserver.fiterchain;

import java.nio.ByteBuffer;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.sun.simplenioserver.NioSession;

public class ProtocolCodecFilter extends AbstractIoFilter{
	
	private static final Logger logger = LoggerFactory.getLogger(ProtocolCodecFilter.class);
	
	private ProtocolCodecFactory protocolCodecFactory = null;
	
	public ProtocolCodecFilter(String filterName) {
		super(filterName);
		protocolCodecFactory = new ProtocolCodecFactory("UTF-8");
	}
	
	@Override
	public void fireMessageReceived(NextFilter nextFilter, NioSession session,
			ByteBuffer byteBuffer) {
		
		DecoderCodec decoder = protocolCodecFactory.getDecoder();
		Queue<ByteBuffer> queueEncoder = decoder.encode(byteBuffer);
		
		for(ByteBuffer buffer = queueEncoder.poll();buffer!=null;buffer = queueEncoder.poll()){
			logger.info("decoder msg =|{}|",new String(buffer.array(),0,buffer.position()));
			nextFilter.fireMessageReceived(session, buffer);
		}
	}
	
	
}


