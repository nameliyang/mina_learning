package com.ly.sun.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MyController {
	
	@FXML
	TextArea sendTextArea;
	
	@FXML
	public void onKeyPressed(){
		System.out.println("on drag enter...");
	}
	
}
