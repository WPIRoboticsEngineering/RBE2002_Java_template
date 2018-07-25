package com.FieldSimulator.FieldSimulator_2001;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main  extends Application {

	public static void main(String[] args) throws IOException {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainScreen.fxml"));
		primaryStage.setTitle("Field Simulator");
		root.getStylesheets().add("/materialfx-material-design-for-javafx/material-fx-v0_3.css");
		primaryStage.setScene(new Scene(root, 1000, 375));
	    primaryStage.setResizable(false);

		primaryStage.show();
		primaryStage.getScene().setRoot(root);
	}

}
