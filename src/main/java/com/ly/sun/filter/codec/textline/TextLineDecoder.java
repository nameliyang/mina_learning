package com.ly.sun.filter.codec.textline;

import com.ly.sun.core.buffer.IoBuffer;
import com.ly.sun.filter.codec.ProtocolDecoder;
import com.ly.sun.filter.codec.ProtocolDecoderOutput;
import com.ly.sun.transport.socket.nio.NioSocketSession;

public class TextLineDecoder implements ProtocolDecoder{
	
	@Override
	public void decode(NioSocketSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		
		
	}
	
	
	public void decodeAuto(Context context,NioSocketSession session,IoBuffer in,ProtocolDecoderOutput out){
		int oldPos = in.position();
		int oldLimit = in.limit();
		int matchCount = context.getMatchCount();
		
		while(in.hasRemaining()){
			boolean matched = false;
			byte b = in.get();
			switch (b) {
			case '\r':
				matchCount++;
			case '\n':
				matchCount++;
				matched = true;
				break;
			default:
				matchCount = 0;
				break;
			}
			
			if(matched){
				int pos = in.position();
				in.limit(pos);
				in.position(oldPos);
				context.append(in);
				in.position(pos);
				in.limit(oldLimit);
				
				IoBuffer ioBuffer = context.getIoBuffer();
				ioBuffer.flip();
				ioBuffer.limit(ioBuffer.limit() - matchCount);
				
				byte[] data = new byte[ioBuffer.limit()];
				
			}
		}
	}
	
	private class Context{
		
		private final IoBuffer buf;
		
		public Context(int bufferLength){
			buf = IoBuffer.allocate(bufferLength).setAutoExpand(true);
		}
		
		
		public int getMatchCount() {
			return 0;
		}

		public void append(IoBuffer ioBuffer){
			getIoBuffer().put(buf);
		}
		
		public IoBuffer getIoBuffer(){
			return buf;
		}
	}
}	


