package com.ly.sun.gui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
//github.com/nameliyang/mina_learning.git
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
	
	
	@FXML
	public void onConnect() throws UnknownHostException, IOException{
		String ipStr = ipField.getText();
		int port = Integer.parseInt(portField.getText());
		socket = new Socket(ipStr, port);
		logger.info("connect {}:{}",ipStr,port);
		thread.start();
	}
	
	
	@FXML
	public void onDisConnect() throws IOException{
		if(socket != null){
			logger.info("disconnect.....");
			socket.close();
		}
	}
	int index = 0;
	
	@FXML
	public void send(ActionEvent event) throws IOException{
		if(msgBuffer == null){
			msgBuffer = sendTextArea.getText().getBytes();
		}
		int bufferSize = Integer.parseInt(writeBufField.getText());
		OutputStream outputStream = socket.getOutputStream();
		
		if(index + bufferSize -1 >= msgBuffer.length ){
			bufferSize = msgBuffer.length -1  - index +1;
			logger.info("buffersize = {}",bufferSize);
		}
		if(bufferSize == 0){
			logger.info("end send server...");
			return;
		}
		logger.info("do write msgBuffer index = {},bufferSize={}",index,bufferSize);
		outputStream.write(msgBuffer,index,bufferSize);
		logger.info("send msg ={}",new String(msgBuffer,index,bufferSize));
		index = index+bufferSize;
		outputStream.flush();
	}
	
	private static final Object lock = new Object();
	boolean readFlag = false;
	@FXML
	public void doread(ActionEvent event){
		synchronized (lock) {
			readFlag  = true;
			logger.info("set readflag = true and notify wait thread");
			lock.notify();
		}
	}
	
	Thread thread = null;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		new Thread(){
			public void run() {
				try {
					SimpleServer server = new SimpleServer(23);
					logger.info("start simple listener server");
					server.startListener();
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
		
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
