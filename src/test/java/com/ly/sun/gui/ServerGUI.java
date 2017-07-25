package com.ly.sun.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerGUI extends Application {
	
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

}
