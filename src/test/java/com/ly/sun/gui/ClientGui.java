package com.ly.sun.gui;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientGui extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		URL resource = getClass().getResource("server.fxml");
		Parent root = FXMLLoader.load(resource);
		Scene scene = new Scene(root);
		stage.setTitle("FXML Welcome");
		stage.setScene(scene);
		stage.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
