package com.ly.sun.filter.codec.textline;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import com.ly.sun.core.buffer.IoBuffer;
import com.ly.sun.filter.codec.ProtocolDecoder;
import com.ly.sun.filter.codec.ProtocolDecoderOutput;
import com.ly.sun.transport.socket.nio.NioSocketSession;

public class TextLineDecoder implements ProtocolDecoder{
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
	public void decode(NioSocketSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		
		Context context = getContext(session);
		
		decode(session, in, out);
		
	}
	
	
	private Context getContext(NioSocketSession session) {
		Object context = session.getAttribute(CONTEXT);
		if(context == null){
			  context = new Context(bufferLength);
			  session.setAttributeIfAbsent(CONTEXT, context);
		}
		return (Context) session.getAttribute(CONTEXT);
	}

	public void decodeAuto(Context context,NioSocketSession session,IoBuffer in,ProtocolDecoderOutput out) throws CharacterCodingException{
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
				
				try{
					byte[] data = new byte[ioBuffer.limit()];
					ioBuffer.get(data);
					CharsetDecoder decoder = context.getDecoder();
					CharBuffer charBuffer = decoder.decode(ByteBuffer.wrap(data));
				    String str = charBuffer.toString();
	                writeText(session, str, out);
				}finally{
					ioBuffer.clear();
				}
				
			}
		}
	}
	
	 private void writeText(NioSocketSession session, String text, ProtocolDecoderOutput out) {
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
			getIoBuffer().put(buf);
		}
		
		public IoBuffer getIoBuffer(){
			return buf;
		}
		
		public CharsetDecoder getDecoder(){
			return decoder;
		}
	}
}	


