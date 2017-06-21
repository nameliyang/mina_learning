package com.ly.sun.filter.codec;

import java.util.LinkedList;
import java.util.Queue;

import com.ly.sun.core.filterchain.IoFilter.NextFilter;
import com.ly.sun.core.session.IoSession;

public abstract class AbstractProtocolDecoderOutput implements ProtocolDecoderOutput { 
	
	Queue<Object> msgQueue = new LinkedList<Object>();
	
	@Override
	public void flush(NextFilter nextFilter, IoSession session) {
		for(Object msg = msgQueue.poll();msg!=null;msg = msgQueue.poll()){
			nextFilter.messageReceived(session, msg);
		}
	}
	
	@Override
	public void write(Object msg) {
		msgQueue.add(msg);
	}
	
	public Queue<Object> getMessages(){
		return msgQueue;
				
	}

}
