package com.ly.sun.transport.socket.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import com.ly.sun.core.buffer.IoBuffer;
import com.ly.sun.core.filterchain.DefaultIoFilterChain;
import com.ly.sun.core.filterchain.IoFilterChain;
import com.ly.sun.core.session.IoSession;
import com.ly.sun.core.session.IoSessionConfig;
import com.ly.sun.filter.codec.ProtocolCodecFilter;
import com.ly.sun.filter.codec.textline.TextLineProtocolCodecFactory;
import com.ly.sun.util.NamePerservingRunnable;

public class NioProcessor {
	
	private static final Queue<NioSocketSession> newSessions = new ConcurrentLinkedQueue<NioSocketSession>();
	
	private AtomicReference<Processor> processorRef = new AtomicReference<Processor>();
	
	private final Executor executor;
	
	Selector selector ;
	
	NioSocketAcceptor nioSocketAcceptor;
	public NioProcessor(Executor executor, NioSocketAcceptor nioSocketAcceptor) throws IOException{
		this(executor,Selector.open());
		this.nioSocketAcceptor = nioSocketAcceptor;
	}
	
	public NioProcessor(Executor executor,Selector selecor){
		if(executor ==  null){
			throw new IllegalArgumentException();
		}
		this.executor = executor;
		this.selector = selecor;
	}
	
	public void add(NioSocketSession nioSession) {
		 newSessions.add(nioSession);
		 startupProcessor();
	}
	
	
	public void startupProcessor(){
		Processor processor = processorRef.get();
		if(processor == null){
			processor = new Processor();
			if(processorRef.compareAndSet(null, processor)){
				wakeup();
				executor.execute(new NamePerservingRunnable(processor, "ProcessorThread"));
			}
		}
	}
	
	public void wakeup(){
		selector.wakeup();
	}
	
	class Processor implements Runnable{
		
		@Override
		public void run() {
			for(;;){
				try {
					int select = selector.select();
					int nSessions = handlerNewSessioins();
					
					if(select >0){
						process();
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}

	}
	
	private void process() {
		IoSessionIterator<NioSocketSession> ioSessionIterator 
			= new IoSessionIterator<NioSocketSession>(selector.selectedKeys());
		
		while(ioSessionIterator.hasNext()){
			NioSocketSession session = ioSessionIterator.next();
			ioSessionIterator.remove();
			process(session);
		}
	}
	
	
	
	public void process(NioSocketSession session) {
		if(isReadable(session)){
			read(session);
		}
		if(isWritable(session)){
			
		}
	}

	private void read(NioSocketSession session) {
		IoSessionConfig config = session.getSessionConfig();
		int bufferSize = config.getReadBufferSize();
		IoBuffer buffer = IoBuffer.allocate(bufferSize);
		try{
			int readBytes = 0;
		    int rtn ;
		    try{
		    	while((rtn = read(session,buffer)) >0 ){
					readBytes += rtn;
					if(!buffer.hasRemaining()){
						break;
					}
				}
		    }finally{
		    	buffer.flip();
		    }
			if(readBytes > 0){
				IoFilterChain ioFilterChain = session.getIoFilterChain();
				ioFilterChain.fireMessageReceived(buffer);
			}
			
		}catch(Exception e){
			
		}
	}

	private int  read(NioSocketSession session,
			IoBuffer buffer) throws IOException {
		SocketChannel socketChannel = session.getChannel();
		return socketChannel.read(buffer.buf());
	}

	private boolean isWritable(NioSocketSession session) {
		
		return false;
	}

	private boolean isReadable(NioSocketSession session) {
		SelectionKey selectionKey = session.getSelectionKey();
		return (selectionKey.isValid() && selectionKey.isReadable());
	}

	class IoSessionIterator<NioSocketSession> implements Iterator<NioSocketSession>{
		 private final Iterator<SelectionKey> iterator;
		 
		public IoSessionIterator(Set<SelectionKey> keys) {
			iterator = keys.iterator();
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public NioSocketSession next() {
			SelectionKey key = iterator.next();
			NioSocketSession nioSession = (NioSocketSession) key.attachment();
			return nioSession;
		}
		
		@Override
		public void remove() {
			iterator.remove();
		}
	}
	
	
	public int handlerNewSessioins(){
		int addedSessions = 0;
		for(NioSocketSession session = newSessions.poll();session!=null;session = newSessions.poll()){
			if(addNow(session)){
				addedSessions++;
			}
		}
		return addedSessions;
	}

	private boolean addNow(IoSession session) {
		boolean registered = false;
		try{
			init(session);
			IoFilterChain ioFilterChain = new DefaultIoFilterChain(session);
		//	ioFilterChain.addLast(new TextLineFilter("textLineDecoder"));
			ioFilterChain.addLast(new ProtocolCodecFilter("textLineFilter", new TextLineProtocolCodecFactory("GBK")));
			
			ioFilterChain.fireSessionCreated();
			session.setIoFilterChain(ioFilterChain);
			session.setIoHandler(nioSocketAcceptor.getHandler());
			registered = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return registered;
	}

	private void init(IoSession session) throws IOException {
		SocketChannel channel = session.getChannel();
		channel.configureBlocking(false);
		session.setSelectionKey(channel.register(selector, SelectionKey.OP_READ,session));
	}
}
