package com.ly.sun.core.write;

import com.ly.sun.core.future.WriteFuture;

public interface WriteRequest {
	
	public WriteFuture getFuture();
	
	public Object getMessage();
	
}
