package com.ly.sun.core.session;

public abstract class AbstractIoSessionConfig implements IoSessionConfig{
	
	private int readBufferSize = 2048;
	

	@Override
	public int getReadBufferSize() {
		return readBufferSize;
	}

	@Override
	public void setReadBufferSize(int readBuffSize) {
		if(readBuffSize <= 0){
			throw new IllegalArgumentException("readBufferdSize:"+readBuffSize);
		}
		this.readBufferSize = readBuffSize;
	}

}
