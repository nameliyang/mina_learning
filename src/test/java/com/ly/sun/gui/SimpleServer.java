package com.ly.sun.gui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
	int port;
	ServerSocket serverSocket = null;

	public SimpleServer(int port) throws IOException {
		this.port = port;
		serverSocket = new ServerSocket(port);
	}

	public void startListener() {
		
		while (true) {
			Socket socket  = null;
			try {
				socket = serverSocket.accept();
				InputStream inputStream = socket.getInputStream();
				OutputStream outputStream = socket.getOutputStream();
				int read = 0;
				while ((read = inputStream.read()) != -1) {
					outputStream.write(read);
				}
			} catch (Exception e) {
				// e.printStackTrace();
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

}
