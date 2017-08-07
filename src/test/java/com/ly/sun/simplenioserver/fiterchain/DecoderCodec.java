package com.ly.sun.simplenioserver.fiterchain;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

public class DecoderCodec {
	
	String charset;
	
	public DecoderCodec(String charset) {
		this.charset = charset;
	}

	public Queue<ByteBuffer> encode(ByteBuffer byteBuffer){
		int oldPos = byteBuffer.position();
		int matchCount  = 0;
		boolean match  = false;
		Queue<ByteBuffer> queue = new LinkedList<>();
		ByteBuffer tempBuffer = ByteBuffer.allocate(byteBuffer.remaining());
		
		while(byteBuffer.hasRemaining()){
			char c = (char) byteBuffer.get();
			switch (c) {
			case '\r':
				matchCount++;
				break;
			case '\n':
				matchCount++;
				match= true;
				break;
			default:
				matchCount=0;
				break;
			}
			tempBuffer.put((byte) c);
			if(match){
				if(matchCount >0){
					int tempPos = tempBuffer.position();
					tempBuffer.position(tempPos-matchCount);
				}
				tempBuffer.flip();
				queue.add(tempBuffer);
				tempBuffer = ByteBuffer.allocate(byteBuffer.remaining());
				oldPos = byteBuffer.position();
				match = false;
			}
		}
		byteBuffer.position(oldPos);
		return queue;
	}
	
	
}
