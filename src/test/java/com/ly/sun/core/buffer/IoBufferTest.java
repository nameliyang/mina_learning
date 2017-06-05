package com.ly.sun.core.buffer;

import java.nio.ByteBuffer;

@SuppressWarnings("all")
public class IoBufferTest {

	public static void main(String[] args) {
		
		IoBuffer buffer = IoBuffer.allocate(3, false).setAutoExpand(true);
		
		buffer.put((byte) 1);
		buffer.put((byte) 2);
		buffer.put((byte) 3);
		buffer.put((byte) 4);
		
		buffer.flip();
		System.out.println(buffer.get());
		System.out.println(buffer.get());
		System.out.println(buffer.get());
		System.out.println(buffer.get());
	}

}
