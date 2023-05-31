package application;

import java.io.IOException;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SceneController {
	
	@FXML
	TextField emailTextField;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void login(ActionEvent event) throws IOException {
		//root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		String email = emailTextField.getText();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PersonalInfo.fxml"));
		root = loader.load();
		
		PersonalInfoController personalInfoController = loader.getController();
		personalInfoController.displayEmail(email);
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToRegistration(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Registration.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void switchToLogin(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void switchToPersonalInfo(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("PersonalInfo.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void switchToRecord(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Record.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void switchToStatusOfUse(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("StatusOfUse.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void switchToScooterInfo(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("ScooterInfo.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void switchToRentInfo(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("RentInfo.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void switchToPay(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Pay.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void switchToCharge(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Charge.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
