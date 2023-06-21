/**
 * The SceneController class is responsible for managing the user interface and handling user interactions.
 * It controls the different scenes and provides methods for user registration, login, and other functionalities.
 */
package application;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.geometry.Insets;
import java.io.IOException;
import org.json.JSONObject;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

public class SceneController {

	// Fields for the different UI elements
	@FXML
	private TextField emailTextField;
	@FXML
	private PasswordField passwordPasswordField;
	@FXML
	private TextField passwordTextField;
	@FXML
	private TextField safeTextField;
	@FXML
	private TextField creditTextField;
	@FXML
	private TextField phoneTextField;
	@FXML
	private TextField nameTextField;

	@FXML
	public Label emailLabel;
	@FXML
	public Label phoneLabel;
	@FXML
	public Label nameLabel;
	@FXML
	public Label creditLabel;
	@FXML
	public Label safeLabel;

	@FXML
	public Label startTime;
	@FXML
	public Label endTime;
	@FXML
	public Label totalMinutes;
	@FXML
	public Label bill;
	@FXML
	public Label card;
	@FXML
	public Label couponLabel;

	@FXML
	public CheckBox couponCheck;

	private Stage stage;
	private Scene scene;
	private Parent root;
	private String email;
	private String name;
	private String password;
	private String phone;
	private String safe;
	private String credit;

	/**
	 * Retrieves and sets the personal information of the user.
	 */
	public void setpersonalinfo() {
		int resCode = API.info();
		if (resCode == 200) {
			nameLabel.setText(API.getResultObject().getString("username"));
			emailLabel.setText(API.getResultObject().getString("email"));
			phoneLabel.setText(API.getResultObject().getString("phoneNumber"));
			creditLabel.setText(API.getResultObject().getString("cardNumber"));
			safeLabel.setText(API.getResultObject().getString("safeNumber"));
			String couponNum = Integer.toString(API.getResultObject().getInt("couponCount"));
			couponLabel.setText("優惠券 ：          " + couponNum + "張");

		}
	}

	/**
	 * Displays the personal information of the user.
	 */
	public void show(ActionEvent event) throws IOException {
		setpersonalinfo();
	}

	/**
	 * Switches to the registration scene.
	 */
	public void switchToRegistration(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Registration.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	/**
	 * Handles the user registration.
	 * When the format requirements are met, registration can be performed normally. 
	 * If the account already exists and the format does not meet the requirements, registration cannot be performed.
	 */
	public void registration(ActionEvent event) throws IOException {

		name = nameTextField.getText();
		password = passwordTextField.getText();
		credit = creditTextField.getText();
		safe = safeTextField.getText();
		phone = phoneTextField.getText();
		email = emailTextField.getText();
		System.out.println(name);
		System.out.println(password);
		System.out.println(credit);
		System.out.println(safe);
		System.out.println(phone);
		System.out.println(email);

		int resCode = API.registration(name, password, credit, safe, phone, email);
		System.out.println(resCode);

		if (resCode == 200) {

			root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} else if (resCode == 409) {
			Stage popupStage = new Stage();
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setTitle("Registration Failed");

			Label messageLabel = new Label("此帳號已存在\n可直接登入");
			messageLabel.setWrapText(true);
			messageLabel.setAlignment(Pos.CENTER);
			messageLabel.setTextAlignment(TextAlignment.CENTER);

			Button closeButton = new Button("確認");
			closeButton.setOnAction(e -> popupStage.close());

			VBox popupRoot = new VBox(messageLabel, closeButton);
			popupRoot.setAlignment(Pos.CENTER);
			popupRoot.setSpacing(10);
			popupRoot.setPadding(new Insets(20));

			Scene popupScene = new Scene(popupRoot, 250, 150);
			popupStage.setScene(popupScene);
			popupStage.showAndWait();
			System.out.println("Login failed");
		} else {
			Stage popupStage = new Stage();
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setTitle("Registration Failed");

			Label messageLabel = new Label("註冊失敗\n請確認資料格式皆符合要求");
			messageLabel.setWrapText(true);
			messageLabel.setAlignment(Pos.CENTER);
			messageLabel.setTextAlignment(TextAlignment.CENTER);

			Button closeButton = new Button("確認");
			closeButton.setOnAction(e -> popupStage.close());

			VBox popupRoot = new VBox(messageLabel, closeButton);
			popupRoot.setAlignment(Pos.CENTER);
			popupRoot.setSpacing(10);
			popupRoot.setPadding(new Insets(20));

			Scene popupScene = new Scene(popupRoot, 250, 150);
			popupStage.setScene(popupScene);
			popupStage.showAndWait();
		}

	}

	/**
	 * Handles the logout event.
	 */
	public void Logout(ActionEvent event) throws IOException {
		int resCode = API.logout();

		if (resCode == 200) {
			root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}

	/**
	 * Handles the logout event without sending a request to the server.
	 */
	public void Logout2(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Switches the scene to the login page.
	 */
	public void switchToLogin(ActionEvent event) throws IOException {

		root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Handles the login event.
	 * When the account password is correct, you can log in normally (confirm the token), 
	 * and you can be judged as a maintenance personnel or a general user.
	 */
	public void login(ActionEvent event) throws IOException {

		name = nameTextField.getText();
		System.out.println(name);
		password = passwordPasswordField.getText();
		System.out.println(password);

		int resCode = API.login(name, password);

		if (resCode == 200) { // 登入成功
			int id = API.getResultObject().getInt("id");
			boolean isRenter = API.getResultObject().getBoolean("renter");
			System.out.println("id: " + id + ", isRenter: " + isRenter);
			if (isRenter) {
				int resCode2 = API.rentingStatus();
				if (resCode2 == 200 && API.getResultObject().getBoolean("renting")) {
					root = FXMLLoader.load(getClass().getResource("StatusOfUse.fxml"));
					stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
					System.out.println("API.getResultObject().getBoolean(\"renting\")");
				} else {
					root = FXMLLoader.load(getClass().getResource("PersonalInfo.fxml"));
					stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
					API.info();
				}
			} else if (!isRenter) {
				root = FXMLLoader.load(getClass().getResource("repairer.fxml"));
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			}
		} else {
			Stage popupStage = new Stage();
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setTitle("Login Failed");

			Label messageLabel = new Label("登入失敗\n請重新確認帳號密碼");
			messageLabel.setWrapText(true);
			messageLabel.setAlignment(Pos.CENTER);
			messageLabel.setTextAlignment(TextAlignment.CENTER);

			Button closeButton = new Button("重新登入");
			closeButton.setOnAction(e -> popupStage.close());

			VBox popupRoot = new VBox(messageLabel, closeButton);
			popupRoot.setAlignment(Pos.CENTER);
			popupRoot.setSpacing(10);
			popupRoot.setPadding(new Insets(20));

			Scene popupScene = new Scene(popupRoot, 250, 150);
			popupStage.setScene(popupScene);
			popupStage.showAndWait();

			System.out.println("Login failed");
		}

	}

	/**
	 * Switches the scene to the personal information page.
	 */
	public void switchToPersonalInfo(ActionEvent event) throws IOException {
		int resCode = API.info();

		if (resCode == 200) {
			root = FXMLLoader.load(getClass().getResource("PersonalInfo.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}

	/**
	 * Switches the scene to the record page.
	 */
	public void switchToRecord(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Record.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Switches the scene to the status of use page.
	 */
	public void switchToStatusOfUse1(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("StatusOfUse.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Switches the scene to the scooter information page.
	 */
	public void switchToScooterInfo(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("carMapDemo.fxml"));
		Parent root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Handles the coupon use event.
	 * Coupons will be used when the checkbpox of [Whether to use coupons] is ticked.
	 */
	public void couponUse(ActionEvent event) throws IOException {
		data.chargeCount--;
		data.couponUse = true;
	}

	/**
	 * Switches the scene to the rent information page.
	 */
	public void switchToRentInfo(ActionEvent event) throws IOException {
		//////////// 取得最終位置，計算距離/////////////////
		root = FXMLLoader.load(getClass().getResource("RentInfo.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	/**
	 * Switches the scene to the payment page.
	 */
	public void switchToPay(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Pay.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Switches the scene to the charging station map page.
	 */
	public void switchToCharge(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("chargeMapDemo.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	AtomicDistance atomicDistance = new AtomicDistance();

	/**
	 * Handles the rent success event.Update the information of driving distance and electric locomotive power.
	 * Execute the addToSum() method every 10 seconds and update the relevant data while navigating to the "StatusOfUse.fxml" scene.
	 * 
	 */
	public void rentSuccess(ActionEvent event) throws IOException {

//		API.rentingStatus();
//		JSONObject rentingStatus = API.getResultObject().getJSONObject("currentRenting");
//		double start_lat = rentingStatus.getDouble("start_lat");
//		double start_lng = rentingStatus.getDouble("start_lng");
//		System.out.println("start_lat :" + start_lat + ", start_lng :" + start_lng);
//		System.out.println("newSite[0] :" + data.lat + ", newSite[1] :" + data.lng);

		Runnable nextLocation = () -> {
			try {
				atomicDistance.addToSum();
				data.totalDis = atomicDistance.getDis();
			} catch (IOException e) {
				e.printStackTrace();
			}
		};

		Thread thread = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(10000); // Wait for 10 seconds
					
					nextLocation.run();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("StatusOfUse.fxml"));
		Parent root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	/**
	 * Display the time of borrowing and returning the car in the interface, 
	 * the total number of minutes borrowed, the cost, and confirm the credit card number to be used for payment
	 */
	public void setrentinfo() {

		API.rentingStatus();
		JSONObject rentingStatus = API.getResultObject().getJSONObject("currentRenting");
		startTime.setText("借車時間：" + rentingStatus.getString("startTime"));
		LocalDateTime timestamp = LocalDateTime.parse(rentingStatus.getString("startTime"),
				DateTimeFormatter.ISO_DATE_TIME);
		endTime.setText("還車時間：" + LocalDateTime.now().toString());
		Duration duration = Duration.between(timestamp, LocalDateTime.now());
		long time = duration.toMinutes();
		totalMinutes.setText("總計 : " + (int) time + " 分鐘");
		bill.setText("費用： " + data.totalDis * 3 + " 元");
		API.info();
		JSONObject p_info = API.getResultObject();
		card.setText("信用卡號： " + p_info.getString("cardNumber"));

	}
	/**
	 * Execute setrentinfo().
	 * @param event
	 * @throws IOException
	 */
	public void showRentInfo(ActionEvent event) throws IOException {
		setrentinfo();
	}
	/**
	 * When returning the car, it will confirm whether the battery of the electric scooter is greater than 20% 
	 * (use the API to connect the back-end code to confirm), 
	 * and update the number of coupons in the record.
	 * @param event
	 * @throws IOException
	 */
	public void payAndReturnCar(ActionEvent event) throws IOException {
		///////// 取得使用過後電量///////////
		System.out.println("現在騎乘距離為 :" + data.totalDis);
		System.out.println("Power is :" + data.charge);
		int resCode = API.returnCar(data.lat, data.lng, data.totalDis, data.charge, data.chargeCount, data.couponUse);
		System.out.println("couponUse :" + data.couponUse);
		System.out.println(resCode);
		if (resCode == 200) {
			root = FXMLLoader.load(getClass().getResource("PersonalInfo.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}
}
