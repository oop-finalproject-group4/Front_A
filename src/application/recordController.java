/**
This class is the controller for the rent record view in the application.
It manages the display of rent records in a table view and handles user actions.
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class recordController {
	@FXML
	TableView<RentRecord> tableView = new TableView<>();
	@FXML
	private TableColumn<RentRecord, String> dateColumn;
	@FXML
	private TableColumn<RentRecord, Double> start_latColumn;
	@FXML
	private TableColumn<RentRecord, Double> start_lngColumn;
	@FXML
	private TableColumn<RentRecord, Double> end_latColumn;
	@FXML
	private TableColumn<RentRecord, Double> end_lngColumn;
	@FXML
	private TableColumn<RentRecord, String> startTimeColumn;
	@FXML
	private TableColumn<RentRecord, String> endTimeColumn;
	@FXML
	private TableColumn<RentRecord, Double> totalMinutesColumn;
	@FXML
	private TableColumn<RentRecord, Integer> chargeCountColumn;
	@FXML
	private TableColumn<RentRecord, Integer> billColumn;
	@FXML
	private TableColumn<RentRecord, Double> distanceColumn;
	@FXML
	private TableColumn<RentRecord, Boolean> usedCouponColumn;

	private Stage stage;
	private Scene scene;
	private Parent root;

	/**
	 * Retrieves the rent records and displays them in the table view.
	 */
	public void listRentRecords(ActionEvent event) throws IOException {

		int resCode = API.getRentRecord();
		System.out.println(resCode);
		int length = API.getResultArray().length();
		System.out.println(length);
		List<RentRecord> rentRecordList = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			JSONObject firstObject = API.getResultArray().getJSONObject(i);
			RentRecord rentRecord = new RentRecord();
			rentRecord.setStart_lat(firstObject.getDouble("start_lat"));
			rentRecord.setStart_lng(firstObject.getDouble("start_lng"));
			rentRecord.setEnd_lat(firstObject.getDouble("end_lat"));
			rentRecord.setEnd_lng(firstObject.getDouble("end_lng"));
			rentRecord.setStartTime(firstObject.getString("startTime"));
			rentRecord.setEndTime(firstObject.getString("endTime"));
			rentRecord.setTotalMinutes(firstObject.getDouble("totalMinutes"));
			rentRecord.setChargeCount(firstObject.getInt("chargeCount"));
			rentRecord.setBill(firstObject.getInt("bill"));
			rentRecord.setDistance(firstObject.getDouble("distance"));
			rentRecord.setUsedCoupon(firstObject.getBoolean("usedCoupon"));
			rentRecordList.add(rentRecord);
		}
		getRentRecords(rentRecordList);
	}

	/**
	 * Sets up the table view with the rent records.
	 */
	public void getRentRecords(List<RentRecord> rentRecordList) {
		// Set the cell value factories for each column
		start_latColumn.setCellValueFactory(new PropertyValueFactory<>("start_lat"));
		start_lngColumn.setCellValueFactory(new PropertyValueFactory<>("start_lng"));
		end_latColumn.setCellValueFactory(new PropertyValueFactory<>("end_lat"));
		end_lngColumn.setCellValueFactory(new PropertyValueFactory<>("end_lng"));
		startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
		endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
		totalMinutesColumn.setCellValueFactory(new PropertyValueFactory<>("totalMinutes"));
		chargeCountColumn.setCellValueFactory(new PropertyValueFactory<>("chargeCount"));
		billColumn.setCellValueFactory(new PropertyValueFactory<>("bill"));
		distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
		usedCouponColumn.setCellValueFactory(new PropertyValueFactory<>("usedCoupon"));

		// Create an ObservableList of rent records
		ObservableList<RentRecord> rentRecordObservableList = FXCollections.observableArrayList();

		// Add all rent records from the rentRecordList to the rentRecordObservableList
		rentRecordObservableList.addAll(rentRecordList);

		// Set the items of the tableView with the rentRecordObservableList
		tableView.setItems(rentRecordObservableList);
	}

	/**
	 * Switches to the personal info view.
	 */
	public void switchToPersonalInfo(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("PersonalInfo.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Initializes the controller and retrieves the rent records on startup.
	 */
	@FXML
	public void initialize() {
		try {
			listRentRecords(new ActionEvent());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
