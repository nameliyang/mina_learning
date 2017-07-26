package com.ly.sun.gui;

import java.io.IOException;
import java.net.URL;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.ResourceBundle;

import sun.nio.ch.ServerSocketAdaptor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerGUI extends Application implements Initializable{

	ServerSocketChannel serverChannel;
	Selector selector;
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent parent = FXMLLoader.load(ServerGUI.class.getResource("server.fxml"));
		Scene scene = new Scene(parent);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			serverChannel = ServerSocketChannel.open();
			serverChannel.configureBlocking(false);
			selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
				
	}

}
