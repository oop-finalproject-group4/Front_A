/**
	This class is the controller for the repair view in the application.
	It manages the display of repair cars in a table view and handles user actions.
*/
package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class RepairController {

	@FXML
	TableView<cars> tableView = new TableView<>();
	@FXML
	private TableColumn<cars, String> noColumn;
	@FXML
	private TableColumn<cars, Double> latColumn;
	@FXML
	private TableColumn<cars, Double> lngColumn;
	@FXML
	private TableColumn<cars, Double> powerColumn;
	@FXML
	private TableColumn<cars, String> carStatusColumn;
	@FXML
	private TextField carNoTextField;

	/**
	 * private TableColumn noColumn ;
	 */
	private Stage stage;
	private Scene scene;
	private Parent root;
	private String carNo;

	/**
	 * Retrieves the list of cars and displays them in the table view.
	 */
	public void getCars(List<cars> carsList) {
		// Set the cell value factories for each column
		noColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
		latColumn.setCellValueFactory(new PropertyValueFactory<>("lat"));
		lngColumn.setCellValueFactory(new PropertyValueFactory<>("lng"));
		powerColumn.setCellValueFactory(new PropertyValueFactory<>("power"));
		carStatusColumn.setCellValueFactory(new PropertyValueFactory<>("carStatus"));

		// Create an ObservableList of cars
		ObservableList<cars> carList = FXCollections.observableArrayList();

		// Add all cars from the carsList to the carList
		carList.addAll(carsList);

		// Set the items of the tableView with the carList
		tableView.setItems(carList);
	}

	public void getcarNo(String carNo) {

	}

	/**
	 * Charges the car with the entered car number to full power.
	 */
	public void chargeToFull(ActionEvent event) throws IOException {
		carNo = carNoTextField.getText();
		int resCode = API.charge(carNo);
		int resCode1 = API.getNoPowerCars();
		System.out.println(resCode);
		if (resCode == 200) {
			List<cars> carsList = new ArrayList<>();
			int length = API.getResultArray().length();
			System.out.println(length);
			for (int i = 0; i < length; i++) {
				JSONObject FirstObject = API.getResultArray().getJSONObject(i);
				cars car = new cars();
				car.setNo(FirstObject.getString("no"));
				car.setLng(FirstObject.getDouble("lng"));
				car.setLat(FirstObject.getDouble("lat"));
				car.setPower(FirstObject.getDouble("power"));
				car.setCarStatus(FirstObject.getString("carStatus"));
				carsList.add(car);
			}

			getCars(carsList);
		}

		System.out.println(resCode1);
	}

	/**
	 * Changes the status of the car with the entered car number to "BROKEN".
	 */
	public void adjustToBroken(ActionEvent event) throws IOException {
		carNo = carNoTextField.getText();
		int resCode = API.changeStatus("BROKEN", carNo);
		System.out.println(resCode);
		int resCode1 = API.getCarsStatus("BROKEN");
		System.out.println(resCode);

		if (resCode == 200) {
			List<cars> carsList = new ArrayList<>();
			int length = API.getResultArray().length();
			System.out.println(length);
			for (int i = 0; i < length; i++) {
				JSONObject FirstObject = API.getResultArray().getJSONObject(i);
				cars car = new cars();
				if (FirstObject.getString("no").equals(carNo)) {
					car.setNo(FirstObject.getString("no"));
					car.setLng(FirstObject.getDouble("lng"));
					car.setLat(FirstObject.getDouble("lat"));
					car.setPower(FirstObject.getDouble("power"));
					car.setCarStatus(FirstObject.getString("carStatus"));
					carsList.add(car);
				}

			}
			getCars(carsList);
		}
		System.out.println(resCode1);
	}

	/**
	 * Changes the status of the car with the entered car number to "NORMAL".
	 */
	public void adjustToNormal(ActionEvent event) throws IOException {
		carNo = carNoTextField.getText();
		int resCode = API.changeStatus("NORMAL", carNo);
		System.out.println(resCode);
		int resCode1 = API.getCarsStatus("NORMAL");
		System.out.println(resCode);

		if (resCode == 200) {
			List<cars> carsList = new ArrayList<>();
			int length = API.getResultArray().length();
			System.out.println(length);
			for (int i = 0; i < length; i++) {
				JSONObject FirstObject = API.getResultArray().getJSONObject(i);
				cars car = new cars();
				if (FirstObject.getString("no").equals(carNo)) {
					System.out.println("success");
					car.setNo(FirstObject.getString("no"));
					car.setLng(FirstObject.getDouble("lng"));
					car.setLat(FirstObject.getDouble("lat"));
					car.setPower(FirstObject.getDouble("power"));
					car.setCarStatus(FirstObject.getString("carStatus"));
					carsList.add(car);
				}

			}
			getCars(carsList);
		}
		System.out.println(resCode1);
	}

	/**
	 * Changes the status of the car with the entered car number to "REPAIR".
	 */
	public void adjustToRepair(ActionEvent event) throws IOException {
		carNo = carNoTextField.getText();
		int resCode = API.changeStatus("REPAIR", carNo);
		System.out.println(resCode);
		int resCode1 = API.getCarsStatus("REPAIR");
		System.out.println(resCode1);

		if (resCode == 200) {
			List<cars> carsList = new ArrayList<>();
			int length = API.getResultArray().length();
			System.out.println(length);
			for (int i = 0; i < length; i++) {
				JSONObject FirstObject = API.getResultArray().getJSONObject(i);
				cars car = new cars();
				if (FirstObject.getString("no").equals(carNo)) {
					System.out.println("success");
					car.setNo(FirstObject.getString("no"));
					car.setLng(FirstObject.getDouble("lng"));
					car.setLat(FirstObject.getDouble("lat"));
					car.setPower(FirstObject.getDouble("power"));
					car.setCarStatus(FirstObject.getString("carStatus"));
					carsList.add(car);
				}

			}
			getCars(carsList);
		}
		System.out.println(resCode1);
	}

	/**
	 * Retrieves and displays the list of cars with the status "REPAIR".
	 */
	public void listRepairCars(ActionEvent event) throws IOException {

		int resCode = API.getCarsStatus("REPAIR");
		System.out.println(resCode);
		int length = API.getResultArray().length();
		System.out.println(length);

		List<cars> carsList = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			JSONObject FirstObject = API.getResultArray().getJSONObject(i);
			cars car = new cars();
			car.setNo(FirstObject.getString("no"));
			car.setLng(FirstObject.getDouble("lng"));
			car.setLat(FirstObject.getDouble("lat"));
			car.setPower(FirstObject.getDouble("power"));
			car.setCarStatus(FirstObject.getString("carStatus"));
			carsList.add(car);
		}
		getCars(carsList);
	}

	/**
	 * Retrieves and displays the list of cars with the status "NORMAL".
	 */
	public void listNormalCars(ActionEvent event) throws IOException {

		int resCode = API.getCarsStatus("NORMAL");
		System.out.println(resCode);
		int length = API.getResultArray().length();
		System.out.println(length);
		List<cars> carsList = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			JSONObject FirstObject = API.getResultArray().getJSONObject(i);
			cars car = new cars();
			car.setNo(FirstObject.getString("no"));
			car.setLng(FirstObject.getDouble("lng"));
			car.setLat(FirstObject.getDouble("lat"));
			car.setPower(FirstObject.getDouble("power"));
			car.setCarStatus(FirstObject.getString("carStatus"));
			carsList.add(car);
		}
		getCars(carsList);
	}

	/**
	 * Retrieves and displays the list of cars with the status "BROKEN".
	 */
	public void listBrokenCars(ActionEvent event) throws IOException {
		int resCode = API.getCarsStatus("BROKEN");
		System.out.println(resCode);
		int length = API.getResultArray().length();
		System.out.println(length);

		List<cars> carsList = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			JSONObject FirstObject = API.getResultArray().getJSONObject(i);
			cars car = new cars();
			car.setNo(FirstObject.getString("no"));
			car.setLng(FirstObject.getDouble("lng"));
			car.setLat(FirstObject.getDouble("lat"));
			car.setPower(FirstObject.getDouble("power"));
			car.setCarStatus(FirstObject.getString("carStatus"));
			carsList.add(car);
		}
		getCars(carsList);
	}

	/**
	 * Retrieves and displays the list of cars with no power.
	 */
	public void listnoPowerCars(ActionEvent event) throws IOException {
		int resCode = API.getNoPowerCars();
		System.out.println(resCode);
		int length = API.getResultArray().length();
		System.out.println(length);
		List<cars> carsList = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			JSONObject FirstObject = API.getResultArray().getJSONObject(i);
			cars car = new cars();
			car.setNo(FirstObject.getString("no"));
			car.setLng(FirstObject.getDouble("lng"));
			car.setLat(FirstObject.getDouble("lat"));
			car.setPower(FirstObject.getDouble("power"));
			car.setCarStatus(FirstObject.getString("carStatus"));
			carsList.add(car);
		}

		getCars(carsList);
	}

	/**
	 * Logs out the user and returns to the login view.
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

}
