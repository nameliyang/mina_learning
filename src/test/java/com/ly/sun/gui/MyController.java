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
		if(bufferSize + index > msgBuffer.length){
			bufferSize = index >= msgBuffer.length?0:(msgBuffer.length-index);
			if(bufferSize==0){
				return;
			}
		}
		logger.info("do write msgBuffer index = {},bufferSize={}",index,bufferSize);
		outputStream.write(msgBuffer,index,bufferSize);
		logger.info("send msg ={}",new String(msgBuffer,index,bufferSize));
		index = index+bufferSize;
		outputStream.flush();
	}
	
	@FXML
	public void doread(){
		try {
			InputStream inputStream = socket.getInputStream();
			byte[] buffer = new byte[1024];
			int readLength = 0;
			while((readLength = inputStream.read(buffer))!=-1){
				String readStr = new String(buffer,0,readLength);
				logger.info("read msg = {}",readStr);
				reciveArea.appendText(readStr);
			}
		}catch(Exception e){
			
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	
	
}
