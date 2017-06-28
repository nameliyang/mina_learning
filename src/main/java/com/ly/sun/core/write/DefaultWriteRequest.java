package com.ly.sun.core.write;

import com.ly.sun.core.future.WriteFuture;

public class DefaultWriteRequest implements WriteRequest{
	
	Object message;
	
	WriteFuture writeFuture;
	
	public   DefaultWriteRequest(WriteFuture writeFuture,Object message) {
		this.message = message;
		this.writeFuture = writeFuture;
	}
	
	@Override
	public WriteFuture getFuture() {
		return writeFuture;
	}

	@Override
	public Object getMessage() {
		return message;
	}
	
	
	
}
