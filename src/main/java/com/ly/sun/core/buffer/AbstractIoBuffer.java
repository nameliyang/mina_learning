package com.ly.sun.core.buffer;

import java.nio.ByteBuffer;

public abstract class AbstractIoBuffer extends IoBuffer {
	
	private boolean autoExpand;
	private boolean reCapacityAllowd = true;
	
	private int minimumCapacity ;
	
	public AbstractIoBuffer(int minimumCapacity){
		this.minimumCapacity = minimumCapacity;
	}
	
	@Override
	public boolean isDirect() {
		return buf().isDirect();
	}
	
	@Override
	public boolean isReadOnly() {
		return buf().isReadOnly();
	}
	
	@Override
	public IoBuffer expand(int expectdRemaining) {
		return expand(position(),expectdRemaining);
	}
	
	public IoBuffer expand(int expectdRemaining,boolean autoExpand){
		return expand(position(),expectdRemaining,autoExpand);
	}
	
	@Override
	public IoBuffer expand(int pos, int expectedRemaining) {
		return expand(pos,expectedRemaining,false);
	}
	
	@Override
	public IoBuffer capacity(int newCapacity) {
		if(newCapacity > capacity()	){
			ByteBuffer buffer = buf();
			int pos = buffer.position();
			int limit = buffer.limit();
			buffer.clear();
			ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
			newBuffer.put(buffer);
			buf(newBuffer);
			position(pos);
			limit(limit);
		}
		return this;
	}
	
	public IoBuffer expand(int pos,int expectRemaining,boolean autoExpand){
		int end = pos +expectRemaining;
		int expectEnd ;
		if(autoExpand){
			int highestEnd = Integer.highestOneBit(end);
			expectEnd = highestEnd<end?highestEnd<<1:highestEnd;
		}else{
			expectEnd = end;
		}
		int limit = limit();
		if(expectEnd > capacity()){
			capacity(expectEnd);
//			ByteBuffer buffer = buf();
//			buffer.clear();
//			ByteBuffer newBuffer = ByteBuffer.allocate(expectEnd);
//			newBuffer.put(buffer);
//			buf(newBuffer);
//			position(pos);
		}
		if(expectEnd > limit){
			limit(expectEnd);
		}
		return this;
	}
	
	@Override
	public IoBuffer shrink() {
		int capacity = capacity();
		int limit = limit();
		// 如果limit==capacity 则返回 不做任何修改
		if(limit ==  capacity){
			return this;
		}
		
		int newCapacity = capacity;
		int shinkCapacity = Math.max(minimumCapacity, limit);
		
		while(newCapacity  > shinkCapacity){
			newCapacity>>=1;
		}
		newCapacity = Math.max(shinkCapacity, newCapacity);
		
		ByteBuffer oldBuf = buf();
		int oldPos = oldBuf.position();
		int oldlimit = oldBuf.limit();
		oldBuf.clear();
		ByteBuffer newBuffer = oldBuf.isDirect()?ByteBuffer.allocateDirect(newCapacity):ByteBuffer.allocate(newCapacity);
		newBuffer.put(oldBuf);
		newBuffer.position(oldPos);
		newBuffer.limit(oldlimit);
		buf(newBuffer);
		return this;
	}
	
	@Override
	public int limit() {
		return buf().limit();
	}

	@Override
	public IoBuffer limit(int limit) {
		buf().limit(limit);
		return this;
	}

	@Override
	public int capacity() {
		return buf().capacity();
	}
	
	@Override
	public int position() {
		return buf().position();
	}
	
	@Override
	public IoBuffer position(int pos) {
		buf().position(pos);
		return this;
	}
	
	@Override
	public IoBuffer flip() {
		buf().flip();
		return this;
	}

	@Override
	public IoBuffer put(byte b) {
		autoExpand(1);
		buf().put(b);
		return this;
	}

	private IoBuffer autoExpand(int expectedRemaining) {
		if(isAutoExpand()){
			expand(expectedRemaining,true);
		}
		return this;
	}
	
	private IoBuffer autoExpand(int pos,int expectedRemaining){
		if(isAutoExpand()){
			expand(pos,expectedRemaining,true);
		}
		return this;
	}
	
	
	@Override
	public IoBuffer put(int index, byte b) {
		autoExpand(index,1);
		buf().put(index, b);
		return this;
	}

	@Override
	public IoBuffer put(ByteBuffer buffer) {
		autoExpand(buffer.remaining());
		buf().put(buffer);
		return this;
	}

	@Override
	public IoBuffer put(byte[] buffer) {
		autoExpand(buffer.length);
		buf().put(buffer);
		return this;
	}

	@Override
	public IoBuffer put(byte[] buffer, int offset, int length) {
		autoExpand(length);
		buf().put(buffer,offset,length);
		return this;
	}

	@Override
	public byte get(int index) {
		return buf().get(index);
	}
	
	@Override
	public byte get() {
		return buf().get();
	}
	@Override
	public boolean isAutoExpand() {
		return autoExpand;
	}
	
	@Override
	public IoBuffer setAutoExpand(boolean autoExpand) {
		if(!reCapacityAllowd){
			throw new IllegalStateException("");
		}
		this.autoExpand = autoExpand;
		return this;
	}
	
	@Override
	public boolean hasRemaining() {
		return buf().hasRemaining();
	}
	
	@Override
	public IoBuffer put(IoBuffer buf) {
		return put(buf.buf());
	}
	
	@Override
	public IoBuffer get(byte[] dst) {
		return get(dst,0,dst.length);
	}
	
	@Override
	public IoBuffer get(byte[] dst, int offset, int length) {
		buf().get(dst, offset, length);
		return this;
	}
	
	@Override
	public int remaining() {
		return buf().remaining();
	}
	
	@Override
	public IoBuffer clear() {
		buf().clear();
		return this;
	}
	@Override
	public IoBuffer compact() {
		buf().compact();
		return this;
	}
}
