package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class chargeMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try { 
			Parent root = FXMLLoader.load(getClass().getResource("chargeMapDemo.fxml"));
			Scene scene = new Scene(root,1334,1000);
			primaryStage.setScene(scene);
			primaryStage.setTitle("ChargingPage");
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}



