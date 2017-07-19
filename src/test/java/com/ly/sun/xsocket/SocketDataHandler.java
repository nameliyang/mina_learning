package com.ly.sun.xsocket;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;
import org.xsocket.connection.INonBlockingConnection;

public class SocketDataHandler implements IDataHandler, IConnectHandler, IDisconnectHandler {
	private Set<INonBlockingConnection> sessions = Collections.synchronizedSet(new HashSet<INonBlockingConnection>());

	public boolean onData(INonBlockingConnection nbc)
			throws IOException, BufferUnderflowException, ClosedChannelException, MaxReadSizeExceededException {
		try {
			String data = nbc.readStringByDelimiter("\r\n");
			// Expected Message Format:
			// USERNAME~MESSAGE
			// ie: JohnDoe~Hello World
			if (data.trim().length() > 0) {
				System.out.println("Incoming data: " + data);

				if (data.equalsIgnoreCase("<policy-file-request/>")) {
					nbc.write(
							"<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"8090\"/></cross-domain-policy>\0");
					return true;
				}

				String[] message = data.split("~");

				sendMessageToAll(message[0], message[1]);

				if (message[1].equalsIgnoreCase("SHUTDOWN"))
					Main.shutdownServer();
			}
		} catch (Exception ex) {
			System.out.println("onData: " + ex.getMessage());
		}

		return true;
	}

	private void sendMessageToAll(String user, String message) {
		try {
			synchronized (sessions) {
				Iterator<INonBlockingConnection> iter = sessions.iterator();

				while (iter.hasNext()) {
					INonBlockingConnection nbConn = (INonBlockingConnection) iter.next();

					if (nbConn.isOpen())
						nbConn.write("<b>" + user + "</b>: " + message + "<br />\0");
				}
			}

			System.out.println("Outgoing data: " + user + ": " + message);
		} catch (Exception ex) {
			System.out.println("sendMessageToAll: " + ex.getMessage());
		}
	}

	public boolean onConnect(INonBlockingConnection nbc)
			throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
		try {
			synchronized (sessions) {
				sessions.add(nbc);
			}

			System.out.println("onConnect");
		} catch (Exception ex) {
			System.out.println("onConnect: " + ex.getMessage());
		}

		return true;
	}

	public boolean onDisconnect(INonBlockingConnection nbc) throws IOException {
		try {
			synchronized (sessions) {
				sessions.remove(nbc);
			}

			System.out.println("onDisconnect");
		} catch (Exception ex) {
			System.out.println("onDisconnect: " + ex.getMessage());
		}

		return true;
	}
}