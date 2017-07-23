package com.ly.sun.gui;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ResourceBundle;

import javafx.application.Platform;
//github.com/nameliyang/mina_learning.git
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyController implements Initializable{
	
	private static final Logger logger = LoggerFactory.getLogger(MyController.class);
	
	@FXML
	TextArea sendTextArea;
	
	@FXML
	TextField writeBufField;
	
	@FXML
	TextField readBufField;
	
	@FXML
	TextArea reciveArea;
	
	@FXML
	Button  connBtn;
	
	@FXML
	Button disConn;
	
	@FXML
	TextField ipField;
	
	@FXML
	TextField portField;
	
	Socket socket  = null;
	
	byte[] msgBuffer ;
	
	SocketChannel socketChannel = null;
	@FXML
	public void onConnect() throws UnknownHostException, IOException{
		String ipStr = ipField.getText();
		int port = Integer.parseInt(portField.getText());
		socket = new Socket(ipStr, port);
		logger.info("connect {}:{}",ipStr,port);
		socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.connect(new InetSocketAddress("127.0.0.1", 23));
		// block wait connnect...
		socketChannel.finishConnect();
	//	thread.start();
	}
	
	
	@FXML
	public void onDisConnect() throws IOException{
		if(socket != null){
			logger.info("disconnect.....");
			socket.close();
		}
	}
	int index = 0;
	
	ByteBuffer buffer = null;
	
	
	@FXML
	public void nopSend() throws IOException{
		String content =  writeBufField.getText();
		ByteBuffer buffer = ByteBuffer.wrap(new byte[Integer.parseInt(content)]);
		int writeBytes = socketChannel.write(buffer);
		logger.info("nopper write {} bytes",writeBytes);
	}
	
	
	@FXML
	public void send(ActionEvent event) throws IOException{
		if(buffer == null){
			String content = sendTextArea.getText();
			buffer = ByteBuffer.wrap(content.getBytes());
		}
		if(!buffer.hasRemaining()){
			logger.info("buffer has no remainging ...");
			return ;
		}
		int pos = buffer.position();
		int limit = buffer.limit();
		int expectWriteBytes = Integer.parseInt(writeBufField.getText());
		boolean overFlow = (pos + expectWriteBytes-1 >= buffer.limit());
		if(overFlow && !buffer.hasRemaining()){
			return ;
		}
		int writeBytes = 0;
		if(overFlow){
			writeBytes = socketChannel.write(buffer);
			logger.info("overflow writeBytes ={}",writeBytes);
		}else{
			try{
				buffer.limit(pos+ expectWriteBytes);
				writeBytes  = socketChannel.write(buffer);
				logger.info("writebytes = {}",writeBytes);
			}finally{
				buffer.limit(limit);
			}
		}
		
//		if(msgBuffer == null){
//			msgBuffer = sendTextArea.getText().getBytes();
//		}
//		int bufferSize = Integer.parseInt(writeBufField.getText());
//		OutputStream outputStream = socket.getOutputStream();
//		
//		if(index + bufferSize -1 >= msgBuffer.length ){
//			bufferSize = msgBuffer.length -1  - index +1;
//			logger.info("buffersize = {}",bufferSize);
//		}
//		if(bufferSize == 0){
//			logger.info("end send server...");
//			return;
//		}
//		logger.info("do write msgBuffer index = {},bufferSize={}",index,bufferSize);
//		outputStream.write(msgBuffer,index,bufferSize);
//		logger.info("send msg ={}",new String(msgBuffer,index,bufferSize));
//		index = index+bufferSize;
//		outputStream.flush();
	}
	
	private static final Object lock = new Object();
	boolean readFlag = false;
	@FXML
	public void doread(ActionEvent event) throws IOException{
		ByteBuffer buffer  = ByteBuffer.allocate(Integer.parseInt(readBufField.getText()));
		int read = socketChannel.read(buffer);
		if(read > 0){
			buffer.flip();
			if(buffer.hasRemaining()){
				byte b = buffer.get();
				reciveArea.appendText(String.valueOf(b));
				logger.info("do read = {}",String.valueOf(b));
			}
		}
//		synchronized (lock) {
//			readFlag  = true;
//			logger.info("set readflag = true and notify wait thread");
//			lock.notify();
//		}
	}
	
	
	
	Thread thread = null;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Thread listenerThread = new Thread(){
			public void run() {
				try {
					SingleEchoServer server = new SingleEchoServer(23);
					logger.info("start simple listener server");
					server.startListener();
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		};
		
		listenerThread.setDaemon(true);
		listenerThread.start();
		
		thread = new Thread(){
			@Override
			public void run() {
				logger.info("start read thread...");
				try {
					while(true){
						InputStream inputStream = socket.getInputStream();
						synchronized (lock) {
							while(!readFlag){
								logger.info("start wait");
								lock.wait();
								logger.info("leave wait");
							}
						}
						logger.info("wake up from waitset ,start to do read msgk,buffersize = {}",readBufField.getText());
						byte[] buffer = new byte[Integer.parseInt(readBufField.getText())];
						doRead(inputStream, buffer);
						readFlag = false;
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					readFlag = false;
				}
			
			}

			private void doRead(InputStream inputStream, byte[] buffer) throws IOException {
				int readLength = 0;
				readLength = inputStream.read(buffer);
				String readStr = new String(buffer, 0, readLength);
				logger.info("read msg = {}", readStr);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						reciveArea.appendText(readStr);
					}
				});
			}
		};
		
	}
	
	
}
