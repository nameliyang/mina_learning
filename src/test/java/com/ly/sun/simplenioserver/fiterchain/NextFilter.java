package com.ly.sun.simplenioserver.fiterchain;

import java.nio.ByteBuffer;

import com.ly.sun.simplenioserver.NioSession;

public interface NextFilter {

	void fireMessageReceived(NioSession session, ByteBuffer byteBuffer);

	void fireSessionCreated(NioSession session);

}
