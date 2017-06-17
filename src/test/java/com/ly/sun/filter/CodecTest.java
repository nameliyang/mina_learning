package com.ly.sun.filter;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.util.ArrayList;

public class CodecTest {
	private static void extendCharBuffer(CharBuffer charBuffer) {
		int position = charBuffer.position();
		int limit = charBuffer.limit();
		
		CharBuffer newcharBuffer = CharBuffer.allocate(charBuffer.capacity()<<1);
		charBuffer.clear();
		newcharBuffer.put(charBuffer);
		
	    new ArrayList<String>().contains(null);
	}
	
	
	public static void main(String[] args) throws Exception {
		String path = "C:\\Users\\Administrator\\Desktop\\code.txt";
		FileInputStream file = new FileInputStream(path);
		int bytes = file.available();
		byte[] data = new byte[8];
		int read = file.read(data);
		System.out.println("read num :"+read);
		ByteBuffer buffer = ByteBuffer.wrap(data);
	//	CharsetEncoder newEncoder = Charset.forName("UTF-8").newEncoder();
		CharsetDecoder newDecoder = Charset.forName("UTF-8").newDecoder();
		CharBuffer charBuffer = CharBuffer.allocate(1);
		StringBuilder sb = new StringBuilder();
		
		while(true){
			CoderResult result = newDecoder.decode(buffer, charBuffer, false);
			if(result.isUnderflow()){
				charBuffer.flip();
				while(charBuffer.hasRemaining()){
					sb.append(charBuffer.get());
				}
				charBuffer.clear();
				break;
			}else if(result.isOverflow()){
				charBuffer.flip();
				while(charBuffer.hasRemaining()){
					sb.append(charBuffer.get());
				}
				charBuffer.clear();
				//extendCharBuffer(charBuffer);
			}else if(result.isMalformed()){
				result.throwException();
			}
		}
		
		buffer.compact();
		System.out.println(sb);
		buffer.put((byte) file.read());
		buffer.flip();
		charBuffer.clear();
		newDecoder.decode(buffer, charBuffer, false);
		charBuffer.flip();
		System.out.println(charBuffer.get());
		file.close();
	}
	
}
