package com.ly.sun.simplenioserver.fiterchain;

public class ProtocolCodecFactory {
	
	String charset;
	
	DecoderCodec decoder;
	
	public ProtocolCodecFactory(String charset) {
		this.charset  = charset;
		decoder = new DecoderCodec(charset);
    }
	
	public DecoderCodec getDecoder(){
		return decoder;
	}
	
	

}
