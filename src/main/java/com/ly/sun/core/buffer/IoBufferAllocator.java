package com.ly.sun.core.buffer;

import java.nio.ByteBuffer;

public class IoBufferAllocator {

	public IoBuffer wrap(ByteBuffer byteBuffer) {
		return new SimpleIoBuffer(byteBuffer);
	}

	public IoBuffer allocate(int capacity, boolean useDirectBuffer) {
		return new SimpleIoBuffer(capacity,useDirectBuffer);
	}
	
	
	class SimpleIoBuffer extends AbstractIoBuffer{
		
		private ByteBuffer buf;
		
		public SimpleIoBuffer(ByteBuffer buf) {
			super(buf.capacity());
			this.buf = buf;
		}
		
		public   SimpleIoBuffer(int capacity, boolean useDirectBuffer) {
			super(capacity);
			this.buf = useDirectBuffer?ByteBuffer.allocateDirect(capacity):ByteBuffer.allocate(capacity);
		}
		
		@Override
		public ByteBuffer buf() {
			return buf;
		}

		@Override
		public IoBuffer buf(ByteBuffer buf) {
			this.buf = buf;
			return this;
		}
		
		
	}
}
