package com.ly.sun.gui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
	public static void main(String[] args) throws IOException {
		
		ServerSocket serverSocket = new ServerSocket(23);
		while(true){
			Socket socket = serverSocket.accept();
			try{
				InputStream inputStream = socket.getInputStream();
				OutputStream outputStream = socket.getOutputStream();
				int read = 0;
				while((read = inputStream.read())!=-1){
					outputStream.write(read);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				if(socket != null){
					socket.close();
				}
			}
			
		}
	}
}
