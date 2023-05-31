package application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PersonalInfoController {

	@FXML
	Label emailLabel;
	
	public void displayEmail (String email) {
		emailLabel.setText("電子信箱 " + email);
	}
	
}
