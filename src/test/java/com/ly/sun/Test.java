	package com.ly.sun;
	
	import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.sun.core.service.IoHandlerAdapter;
import com.ly.sun.core.session.IoSession;
import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;
	
public class Test {
		
		private static String calcSHA1(File file) throws FileNotFoundException,
		        IOException, NoSuchAlgorithmException {
		    MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
		    try (InputStream input = new FileInputStream(file)) {
		        byte[] buffer = new byte[8192];
		        int len = input.read(buffer);
		
		        while (len != -1) {
		            sha1.update(buffer, 0, len);
		            len = input.read(buffer);
		        }
		        return new HexBinaryAdapter().marshal(sha1.digest());
		    }
		}
		
		public void copyFile(String source ,String target) throws IOException{
			FileChannel sourceChannel = (FileChannel) Channels.newChannel(new FileInputStream(new File(source)));
			FileChannel targetChannel = (FileChannel) Channels.newChannel(new FileOutputStream(new File(target)));
			targetChannel.transferFrom(sourceChannel, 0, 1024);
		}
		
		public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException {
//			String calcSHA1 = calcSHA1(new File("F:\\dream\\elastic\\kibana-5.5.1-linux-x86.tar.gz"));
//			
//			System.out.println(calcSHA1);
//			System.out.println("47d7707b1b8feb490276fd69b597d27af610d28b");
			
			URL website = new URL("https://artifacts.elastic.co/downloads/kibana/kibana-5.5.1-windows-x86.zip");
			InputStream inputStream = website.openStream();
			OutputStream outputStream = new FileOutputStream(new File("D:/kibana-5.5.1-windows-x86.zip"));
			byte[] buffer = new byte[1024*1024*2];
			
			int length = 0;
			int readBytes = 0;
			
			while((length = inputStream.read(buffer))!=-1){
				outputStream.write(buffer, 0, length);
				readBytes  = readBytes+ length;
				System.out.println("readBytes  = "+readBytes +"bytes,"+readBytes/1024+"k,"+readBytes/1024/1024+"M");
			}
			
			
//			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
//			FileOutputStream fos = new FileOutputStream("D:/test.rar");
//			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			
			
//			SocketChannel socketChannel = SocketChannel.open();
//			socketChannel.configureBlocking(false);
//			Selector selector = Selector.open();
//			socketChannel.register(selector, SelectionKey.OP_CONNECT);
//			socketChannel.connect(new InetSocketAddress("127.0.0.1", 23));
//			
//			while(true){
//				int select = selector.select();
//				if(select >0){
//					Set<SelectionKey> selectedKeys = selector.selectedKeys();
//					Iterator<SelectionKey> iterator = selectedKeys.iterator();
//					while(iterator.hasNext()){
//						SelectionKey key = iterator.next();
//						iterator.remove();
//						if(key.isConnectable()){
//							System.out.println("key is connectablel ...");
//						}
//					}
//				}
//			}
	//		ByteBuffer byteBuffer = ByteBuffer.allocate(10);
	//		byteBuffer.put((byte) '2');
	//		byteBuffer.put((byte)'a');
	//		byteBuffer.flip();// limit = position; posotion = 0;
	//		
	//		byteBuffer.compact(); //
			
	//		char c = '李';
	//		String binaryString = Integer.toBinaryString(c);
	//		System.out.println(binaryString);
	//		System.out.println(Integer.toHexString(c));
	//		OutputStream os = new FileOutputStream("D:/test.txt");
	//		os.write(c&0xff);
	//		os.write(c>>8);
	//		os.flush();
	//		os.close();
			/*NioSocketAcceptor acceptor = new NioSocketAcceptor();
			acceptor.setHandler(new MyIoHandler());
			acceptor.bind(new InetSocketAddress(1234));*/
		}
		
	}
	class MyIoHandler extends IoHandlerAdapter{
		
		Logger logger = LoggerFactory.getLogger(MyIoHandler.class);
		
		public MyIoHandler() {
		}
		@Override
		public void messageReceived(IoSession session, Object msg) throws Exception {
			logger.info("message received ..."+msg);
		}
		
		@Override
		public void sessionCreated(IoSession session) throws Exception {
			logger.info("session created ......");
		}
	}
	
	
	
