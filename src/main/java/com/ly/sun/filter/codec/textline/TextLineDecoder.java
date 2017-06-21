package com.ly.sun.filter.codec.textline;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.sun.core.buffer.IoBuffer;
import com.ly.sun.core.session.IoSession;
import com.ly.sun.filter.codec.ProtocolDecoder;
import com.ly.sun.filter.codec.ProtocolDecoderOutput;

public class TextLineDecoder implements ProtocolDecoder{
	
	private static final Logger logger = LoggerFactory.getLogger(TextLineDecoder.class);
	
	private static final String CONTEXT = "CONTEXT";
    private final Charset charset;
    
    private int bufferLength = 128;
    
    public TextLineDecoder(Charset charset){
    	this.charset = charset;
    }
    
    public TextLineDecoder() {
    	this(Charset.defaultCharset());
	}
    
	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		Context context = getContext(session);
		decodeAuto(context, session, in, out);
	}
	
	
	private Context getContext(IoSession session) {
		Object context = session.getAttribute(CONTEXT);
		if(context == null){
			  context = new Context(bufferLength);
			  session.setAttributeIfAbsent(CONTEXT, context);
		}
		return (Context) session.getAttribute(CONTEXT);
	}

	public void decodeAuto(Context context,IoSession session,IoBuffer in,ProtocolDecoderOutput out) throws CharacterCodingException{
		int oldPos = in.position();
		int oldLimit = in.limit();
		int matchCount = context.getMatchCount();
		
		while(in.hasRemaining()){
			boolean matched = false;
			byte b = in.get();
			switch (b) {
			case '\r':
				matchCount++;
				break;
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
				
				try{
					byte[] data = new byte[ioBuffer.limit()];
					ioBuffer.get(data);
					CharsetDecoder decoder = context.getDecoder();
					CharBuffer charBuffer = decoder.decode(ByteBuffer.wrap(data));
				    String str = charBuffer.toString();
	                writeText(session, str, out);
				}finally{
					oldPos = pos;
					ioBuffer.clear();
				}
				
			}
		}
		in.position(oldPos);
		in.limit(oldLimit);
		if(in.hasRemaining()){
			context.append(in);
		}
	}
	
	 private void writeText(IoSession session, String text, ProtocolDecoderOutput out) {
		 if(logger.isDebugEnabled()){
			 logger.debug("write msg {}",text);
		 }
		 out.write(text);
	 }

	class Context{
		
		private final IoBuffer buf;
		
		private final CharsetDecoder decoder;
		
		public Context(int bufferLength){
			buf = IoBuffer.allocate(bufferLength).setAutoExpand(true);
		    decoder = charset.newDecoder();
		}
		
		public int getMatchCount() {
			return 0;
		}

		public void append(IoBuffer ioBuffer){
			getIoBuffer().put(ioBuffer);
		}
		
		public IoBuffer getIoBuffer(){
			return buf;
		}
		
		public CharsetDecoder getDecoder(){
			return decoder;
		}
	}
}	


