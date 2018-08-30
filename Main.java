package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.image.Image;

public class Main extends Application {
	private static Stage primaryStage;
	private static FlowPane rootLayout;
	public static ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
	public static String activeProOption;
	public static String activeSupplier;
	
	//Initiates the window with its title (start is a default procedure)
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Supplier Manager");
		createWindow("Main.fxml");
	}

	//Applies window updates by fetching fxml file and then applying this as the current window
	public static void createWindow(String filename) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource(filename));
			rootLayout = (FlowPane) loader.load();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
