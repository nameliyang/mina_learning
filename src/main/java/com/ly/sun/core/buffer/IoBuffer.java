package com.ly.sun.core.buffer;

import java.nio.ByteBuffer;

public abstract class IoBuffer {
	
	private static final IoBufferAllocator allocator = new IoBufferAllocator();
	
	public static IoBuffer wrap(ByteBuffer byteBuffer){
		return 	allocator.wrap(byteBuffer);
	}
	
	public static IoBuffer allocate(int capacity,boolean useDirectBuffer){
		return allocator.allocate(capacity,useDirectBuffer);
	}
	
	public static IoBuffer allocate(int capacity){
		return allocate(capacity,false);
	}
	
	public abstract boolean isDirect();
	
	public abstract boolean isReadOnly();
	
	public abstract ByteBuffer buf();
	
	public abstract IoBuffer buf(ByteBuffer buf);
	
	public abstract boolean isAutoExpand();
	
	public abstract IoBuffer shrink();
	
	
	public abstract int limit();
	
	public abstract IoBuffer limit(int limit);
	
	public abstract int capacity();
	
	public abstract IoBuffer capacity(int newCapacity);
	
	public abstract int position();
	
	public abstract IoBuffer position(int pos);
	
	
	public abstract IoBuffer  flip();
	
	public abstract IoBuffer put(byte b);
	
	public abstract IoBuffer put(int index,byte b);
	
	public abstract IoBuffer put(ByteBuffer buffer);
	
	public abstract IoBuffer put(byte[] buffer);
	
	public abstract IoBuffer put(byte[] buffer,int offset,int length);
	
	public abstract byte  get(int index);
	
	public abstract byte get();
	
	public abstract IoBuffer expand(int expectdRemaining);
	
	public abstract IoBuffer expand(int pos,int expectedRemaining);
	
	public abstract IoBuffer setAutoExpand(boolean autoExpand);
	
	public abstract boolean hasRemaining();

	public abstract  IoBuffer put(IoBuffer buf) ;

	public abstract IoBuffer get(byte[] dst,int offset,int length);
	
	public abstract IoBuffer get(byte[] dst);
	
	
	public abstract int remaining() ;

	public abstract IoBuffer clear() ;
	
	@Override
	public String toString() {
		return buf().toString();
	}

	public abstract IoBuffer compact(); 
}
