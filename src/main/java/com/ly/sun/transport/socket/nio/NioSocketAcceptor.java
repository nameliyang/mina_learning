package com.ly.sun.transport.socket.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import com.ly.sun.core.service.IoHandler;
import com.ly.sun.util.NamePerservingRunnable;

public final class NioSocketAcceptor {
	
	private static final Logger logger = Logger.getLogger(NioSocketAcceptor.class.getSimpleName());
	
	SelectorProvider selectorProvider = null;
	
	Selector selector ; 
	
	private boolean reuseAddress = false;
	
	Queue<SocketAddress> localAddresses = new ConcurrentLinkedQueue<SocketAddress>();
	
	private AtomicReference<Acceptor> acceptorRef = new AtomicReference<Acceptor>();
		
	private final static ExecutorService DEFAULT_SERVICE = Executors.newCachedThreadPool();
	
	private ExecutorService service;
	
	private ConcurrentHashMap<SocketAddress, ServerSocketChannel> boundHandlers = new ConcurrentHashMap<SocketAddress, ServerSocketChannel>();
	
	private volatile boolean selectable  = true;
	
	NioProcessor processor;
	
	private IoHandler ioHandler;
	
	public NioSocketAcceptor(ExecutorService service) throws IOException{
		this.service = service;
		 processor = new NioProcessor(service);
		init();
	}
	
	public NioSocketAcceptor() throws IOException{
		this(DEFAULT_SERVICE);
	}
	
	public void setHandler(IoHandler ioHandler){
		if(ioHandler == null){
			throw new IllegalArgumentException("handler cannot be null");
		}
		
		this.ioHandler = ioHandler;
	}
	
	public IoHandler getHandler(){
		return ioHandler;
	}
	
	public void init() throws IOException{
		selector = Selector.open();
	}
	
	public ServerSocketChannel open(SocketAddress socketAddress) throws Exception{
		ServerSocketChannel serverSocketChannel = null;
		if (selectorProvider != null) {
			serverSocketChannel = selectorProvider.openServerSocketChannel();
		} else {
			serverSocketChannel = ServerSocketChannel.open();
		}
		boolean success = false;
		try {

			serverSocketChannel.configureBlocking(false);
			ServerSocket socket = serverSocketChannel.socket();
			socket.setReuseAddress(isReuseAddress());
			try {
				serverSocketChannel.bind(socketAddress);
			} catch (Exception e) {
				serverSocketChannel.close();
				throw e;
			}
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			success = true;
		} finally {
			if(!success){
				close(serverSocketChannel);
			}
		}
		return serverSocketChannel;
	}
	
	public void close(ServerSocketChannel channel) throws Exception{
		SelectionKey key = channel.keyFor(selector);
		if(key != null){
			key.cancel();
		}
		channel.close();
	}
	
	public void bind(SocketAddress localAddress){
		localAddresses.add(localAddress);
		startupAcceptor();
	}
	
	public void startupAcceptor(){
		Acceptor acceptor = acceptorRef.get();
		
		if(acceptor==null){
			acceptor = new Acceptor();
			if(acceptorRef.compareAndSet(null, acceptor)){
				executeTask(acceptor, "acceptorThread");
			}
		}
	}
	
	public void executeTask(Runnable work,String threadName){
		service.execute(new NamePerservingRunnable(work, threadName));
	}
	
	public boolean isReuseAddress(){
		return reuseAddress;
	}
	
	private class Acceptor implements Runnable{
		
		@Override
		public void run() {
			while(selectable){
				try {
					int registerHandlers = registerHandlers();
					if(registerHandlers ==0 ){
						logger.warning("no handlers...");
						break;
					}
					int selected = select();
					
					if(selected >0 ){
						processHandlers(new ServerSocketChannelIterator(selector.selectedKeys()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void processHandlers(ServerSocketChannelIterator iterator) throws IOException{
		while(iterator.hasNext()){
			SelectionKey key = iterator.next();
			iterator.remove();
			
			NioSocketSession nioSession = accept(processor,key);
			
			if(nioSession == null){
				continue;
			}
			
			nioSession.getProcessor().add(nioSession);
			//NioSocketSession session = new NioSocketSession();
			
				
		}
	}
	
	private NioSocketSession accept(NioProcessor processor, SelectionKey key) throws IOException {
		if(!key.isValid()){
			return null;
		}
		ServerSocketChannel socketChannel = (ServerSocketChannel) key.channel();
		SocketChannel ch = socketChannel.accept();
		if(ch == null){
			return null;
		}
		return new NioSocketSession(this,processor,ch);
	}

	private int registerHandlers() {
		
		int bounds = 0;
		try{
			for(SocketAddress localAddress = localAddresses.poll();localAddress !=  null;localAddress = localAddresses.poll()){
				ServerSocketChannel serverChannel = open(localAddress);
				boundHandlers.put(serverChannel.getLocalAddress(), serverChannel);
				bounds++;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
		return bounds;
	}
	
	public int select() throws IOException{
		return selector.select();
	}
	
	class ServerSocketChannelIterator implements Iterator<SelectionKey>{
		
		Iterator<SelectionKey> iterator;
		
		SelectionKey nextKey ;
		
		public  ServerSocketChannelIterator(Collection<SelectionKey> serverSocketChannel) {
			iterator = serverSocketChannel.iterator();
		}
		
		@Override
		public boolean hasNext() {
			while(iterator.hasNext()){
				SelectionKey selectionKey = iterator.next();
				if(selectionKey.isValid()){
					this.nextKey = selectionKey;
					return true;
				}
				iterator.remove();
			}
			return false;
		}

		@Override
		public SelectionKey next() {
			if(nextKey == null){
				throw new NoSuchElementException();
			}
			return nextKey;
		}
		
		@Override
		public void remove() {
			iterator.remove();
		}
	}
	
}
