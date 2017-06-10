package com.ly.sun.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;


public class ReuseSocketTest {
	public static void main(String[] args) throws IOException {
		
		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.bind(new InetSocketAddress(8080));
		
		
		SocketAddress localSocketAddress = channel.socket().getLocalSocketAddress();
		System.out.println(localSocketAddress);
		
		SocketAddress localAddress = channel.getLocalAddress();
		System.out.println(localAddress);
//		ServerSocket serverSocket = new ServerSocket();
//		serverSocket.setReuseAddress(true);
//		int port = 8080;
//		serverSocket.bind(new InetSocketAddress(port));
//		while(true){
//			Socket socket = serverSocket.accept();
//			InputStream inputStream = socket.getInputStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//			String readLine = reader.readLine();
//			PrintWriter pw = new PrintWriter(socket.getOutputStream());
//			pw.println(readLine.toUpperCase());
//			int localPort = socket.getLocalPort();
//		
//			System.out.println("readLine ="+readLine+",port="+localPort+",remotePort="+	socket.getPort());
//			pw.flush();
//			socket.close();
//		}
	}
}

