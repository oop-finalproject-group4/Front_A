/**
 * Controller class for the charge map view.
 */
package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.json.JSONException;
import org.json.JSONObject;

public class chargeMapController implements Initializable {

	// Fields for the different UI elements
	@FXML
	private ImageView mapImageView;
	@FXML
	private Button zoomOutButton;
	@FXML
	private Button zoomInButton;
	@FXML
	private Button moveUpButton;
	@FXML
	private Button moveLeftButton;
	@FXML
	private Button moveRightButton;
	@FXML
	private Button moveDownButton;
	@FXML
	private Button chargeButton;
	@FXML
	private Circle centerPoint;
	@FXML
	public Label LabelChargeNo;
	@FXML
	public Label LabelChargeDist;
	@FXML
	public Label LabelChargeLat;
	@FXML
	public Label LabelChargeLng;
	@FXML
	private AnchorPane container;
	@FXML
	private AnchorPane circlePane;

	private double scaleFactor = 1;
	private double moveAmount = 1;
	private int zoomLevel = 15;
	private String center;
	private int mapWidth = 640;
	private int mapHeight = 640;
	private String apiKey = "AIzaSyBNzyphpX4pmINM8rAP4_eJzu36AbzgW_M";

	private double initialLat = 25.0474925;
	private double initialLng = 121.5391035;
	private double initialRange = 0.013;
	private double circleRadius = 10.0;
	private double MapLat = 25.0474925;
	private double MapLng = 121.5391035;

	@FXML
	private VBox dynamicContainer;

	/**
	 * Initializes the controller.
	 *
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		center = initialLat + "," + initialLng;
		loadMapImage();

		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
			addCharges(initialLat, initialLng);
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	/**
	 * 根據提供的緯度和經度在地圖上添加充電站。
	 *
	 * @param Lat 緯度值。
	 * @param Lng 經度值。
	 */
	private void addCharges(double Lat, double Lng) {
		try {
			List<Node> nodesToRemove = new ArrayList<>();

			for (Node node : circlePane.getChildren()) {
				if (node instanceof Circle) {
					nodesToRemove.add(node);
				}
			}

			circlePane.getChildren().removeAll(nodesToRemove);

			double visibleWidth = container.getWidth() * 0.74;
			double visibleHeight = container.getHeight();
			double maxX = container.getWidth() * 0.74 - circleRadius;
			double maxY = container.getHeight() - circleRadius;

			API.getCharges(Lat, Lng, initialRange);
			API.getResultArray().getJSONObject(0);

			for (int i = 0; i < API.getResultArray().length(); i++) {
				Long chargeNo = API.getResultArray().getJSONObject(i).getLong("no");
				double chargeLat = API.getResultArray().getJSONObject(i).getDouble("lat");
				double chargeLng = API.getResultArray().getJSONObject(i).getDouble("lng");

				String chargeNoValue = String.valueOf(chargeNo);
				String chargeLatValue = String.valueOf(chargeLat);
				String chargeLngValue = String.valueOf(chargeLng);

				double range = initialRange;
				double layoutY = visibleHeight - (chargeLat - (Lat - range)) / (2 * range) * visibleHeight;
				double layoutX = (chargeLng - (Lng - range)) / (2 * range) * visibleWidth;

				double dist = Math.pow((Math.pow((Lat - chargeLat), 2) + Math.pow((Lng - chargeLng), 2)), 0.5);
				String distValue = String.valueOf(Math.round(dist * 1e6) / 8);

				layoutX = Math.max(circleRadius, Math.min(layoutX, maxX));
				layoutY = Math.max(circleRadius, Math.min(layoutY, maxY));

				Circle circle = new Circle(circleRadius);
				circle.setFill(Color.GREEN);
				circle.setLayoutX(layoutX);
				circle.setLayoutY(layoutY);

				circle.setOnMouseClicked(event -> {
					LabelChargeNo.setText(chargeNoValue);
					LabelChargeDist.setText(distValue + " m");
					LabelChargeLat.setText(chargeLatValue);
					LabelChargeLng.setText(chargeLngValue);
				});

				circlePane.getChildren().add(circle);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		loadMapImage();
	}

	private void switchChargePosition(double Lat, double Lng) {
		MapLat = Lat;
		MapLng = Lng;
		System.out.println(Lat + " " + Lng);
	}

	/**
	 * Event handler for the charge button click.
	 */
	@FXML
	private void chargeButtonClicked(ActionEvent event) throws IOException {
		API.rentingStatus();
		JSONObject rentingStatus = API.getResultObject().getJSONObject("currentCar");
		String carNo = rentingStatus.getString("no");
		int carPower = rentingStatus.getInt("power");
		double disOfGoCharge = API.getDistance(data.lat, data.lng, MapLat, MapLng);
		if (data.charge * 2000 - disOfGoCharge > 0) {
			data.charge = 100;
			switchChargePosition(MapLat, MapLng);
			data.lat = MapLat;
			data.lng = MapLng;
			data.totalDis += disOfGoCharge;
			data.chargeCount++;
			Stage popupStage = new Stage();
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setTitle("充電完成");
			String message = String.format("充電完成\n目前電量為：%s%%\n目前優惠券數為：%s", data.charge, data.chargeCount);
			System.out.println(message);
			Label messageLabel = new Label(message);
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
			System.out.println("OK ! Get one coupon.");
		} else {
			Stage popupStage = new Stage();
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setTitle("無法充電");
			String message = String.format("電量不足\n請選擇較近的充電站\n目前電量為：%s%%", data.charge);
			System.out.println(message);
			Label messageLabel = new Label(message);
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
			System.out.println("請選擇較近的充電站");
		}

		FXMLLoader loader = new FXMLLoader(getClass().getResource("StatusOfUse.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Event handler for moving the map down.
	 */
	@FXML
	private void moveDown() {
		circlePane.setTranslateY(circlePane.getTranslateY() + moveAmount * 0.0001);
		double Lat = initialLat += moveAmount * 0.00312;
		double Lng = initialLng;
		MapLat += moveAmount * 0.003;
		loadMapImage();
		addCharges(Lat, Lng);
	}

	/**
	 * Event handler for moving the map up.
	 */
	@FXML
	private void moveUp() {
		circlePane.setTranslateY(circlePane.getTranslateY() - moveAmount * 0.0001);
		double Lat = initialLat -= moveAmount * 0.00312;
		double Lng = initialLng;
		MapLat -= moveAmount * 0.003;
		loadMapImage();
		addCharges(Lat, Lng);
	}

	/**
	 * Event handler for moving the map right.
	 */
	@FXML
	private void moveRight() {
		circlePane.setTranslateY(circlePane.getTranslateY() - moveAmount * 0.0001);
		double Lat = initialLat;
		double Lng = initialLng -= moveAmount * 0.00286;
		MapLng -= moveAmount * 0.003;
		loadMapImage();
		addCharges(Lat, Lng);

	}

	/**
	 * Event handler for moving the map left.
	 */
	@FXML
	private void moveLeft() {
		circlePane.setTranslateY(circlePane.getTranslateY() - moveAmount * 0.0001);
		double Lat = initialLat;
		double Lng = initialLng += moveAmount * 0.00286;
		MapLng += moveAmount * 0.003;
		loadMapImage();
		addCharges(Lat, Lng);

	}

	/**
	 * Event handler for zooming in on the map.
	 */
	@FXML
	private void zoomIn() {
		scaleFactor *= 1.1;
		circlePane.setScaleX(scaleFactor);
		circlePane.setScaleY(scaleFactor);
		mapImageView.setScaleX(scaleFactor);
		mapImageView.setScaleY(scaleFactor);
		circleRadius = circleRadius / 1.1;
		loadMapImage();
		addCharges(MapLat, MapLng);
	}

	/**
	 * Event handler for zooming out on the map.
	 */
	@FXML
	private void zoomOut() {
		scaleFactor /= 1.1;
		circlePane.setScaleX(scaleFactor);
		circlePane.setScaleY(scaleFactor);
		mapImageView.setScaleX(scaleFactor);
		mapImageView.setScaleY(scaleFactor);
		circleRadius = circleRadius * 1.1;
		loadMapImage();
		addCharges(MapLat, MapLng);
	}

	@FXML
	public void showCenterInfo(MouseEvent event) {
		double circleCenterX = centerPoint.getCenterX();
		double circleCenterY = centerPoint.getCenterY();
		double imagePointX = mapImageView.parentToLocal(centerPoint.localToParent(circleCenterX, circleCenterY)).getX();
		double imagePointY = mapImageView.parentToLocal(centerPoint.localToParent(circleCenterX, circleCenterY)).getY();
		System.out.println("X: " + imagePointX + ", Y: " + imagePointY);
	}
	/**
	 * 載入地圖圖像。
	 */
	private void loadMapImage() {
		String mapUrl = "https://maps.googleapis.com/maps/api/staticmap" + "?center=" + center + "&zoom=" + zoomLevel
				+ "&size=" + mapWidth + "x" + mapHeight + "&maptype=roadmap" + "&scale=1.56" + "&key=" + apiKey;
		System.out.println(mapUrl);
		try {
			center = MapLat + "," + MapLng;
			System.out.println(center);
			URL url = new URL(mapUrl);
			InputStream stream = url.openStream();
			Image mapImage = new Image(stream);
			mapImageView.setImage(mapImage);
			;
		} catch (Exception e) {

		}
	}

}