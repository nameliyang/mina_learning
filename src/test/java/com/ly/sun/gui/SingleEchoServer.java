package com.ly.sun.gui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleEchoServer {
	int port;
	ServerSocket serverSocket = null;
	private static final Logger logger = LoggerFactory.getLogger(SingleEchoServer.class);
	public SingleEchoServer(int port) throws IOException {
		this.port = port;
		serverSocket = new ServerSocket(port);
	}

	public void startListener() {
		
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				new Thread(){
					
					public void run() {
						InputStream inputStream;
						try {
							inputStream = socket.getInputStream();
							OutputStream outputStream = socket.getOutputStream();
							int read = 0;
							while ((read = inputStream.read()) != -1) {
								logger.info("server read char ={}",(char)read+"");
								outputStream.write(read);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}finally{
							if (socket != null) {
								try {
									socket.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						
						
					};
				}.start();
			} catch (Exception e) {
				// e.printStackTrace();
			} finally {
				
			}

		}
	}
	
	public static void main(String[] args) throws IOException {
		SingleEchoServer server = new SingleEchoServer(23);
		server.startListener();
	}

}
