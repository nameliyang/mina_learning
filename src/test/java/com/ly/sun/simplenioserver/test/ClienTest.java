package com.ly.sun.simplenioserver.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class ClienTest {
	public static void main(String[] args) {
		CountDownLatch latch = new CountDownLatch(2);
		Thread threadA = new Thread(){
			public void run() {
				try {
					Socket socket = new Socket("127.0.0.1",8080);
					PrintWriter pw =  new PrintWriter(socket.getOutputStream());
					pw.println("a12345674980123456a");
					pw.flush();
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String readLine = reader.readLine();
					System.out.println("readLIne ="+readLine);
				} catch (IOException e) {
					e.printStackTrace();
				};
			};
		};
		
		Thread threadB = new Thread(){
			public void run() {
				try {
					Socket socket = new Socket("127.0.0.1",8080);
					PrintWriter pw =  new PrintWriter(socket.getOutputStream());
					pw.println("b12345674980123456b");
					pw.flush();
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String readLine = reader.readLine();
					System.out.println("readLIne ="+readLine);
				} catch (IOException e) {
					e.printStackTrace();
				};
			};
		};
		threadA.start();
		threadB.start();
	}
}
