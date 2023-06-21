/**
	The carMapController class is the controller for the car map view.
	It handles the user interactions and updates the map and car locations accordingly.
*/
package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class carMapController implements Initializable {
	
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
	private Circle centerPoint;
	@FXML
	public Label LabelCarNo;
	@FXML
	public Label LabelCarPower;
	@FXML
	public Label LabelCarDist;
	@FXML
	public Label LabelCarLat;
	@FXML
	public Label LabelCarLng;
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
	 * Initializes the controller and sets up the initial state of the car map view.
	 * It loads the map image and starts a timeline to periodically update the car
	 * locations.
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
			addCars(initialLat, initialLng, initialRange);
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	/**
	 * Adds car markers to the map based on the provided latitude, longitude, and
	 * range. It retrieves car data using an API and creates circle markers
	 * representing the cars on the map.
	 *
	 * @param Lat The latitude of the center point of the map.
	 * @param Lng The longitude of the center point of the map.
	 * @param rng The range around the center point to consider for car locations.
	 */
	private void addCars(double Lat, double Lng, double rng) {
		try {
			List<Node> nodesToRemove = new ArrayList<>();

			for (Node node : circlePane.getChildren()) {
				if (node instanceof Circle) {
					nodesToRemove.add(node);
				}
			}

			circlePane.getChildren().removeAll(nodesToRemove);

			double visibleWidth = container.getWidth() * 0.75;
			double visibleHeight = container.getHeight();
			double maxX = container.getWidth() * 0.75 - circleRadius;
			double maxY = container.getHeight() - circleRadius;

			API.getCars(Lat, Lng, initialRange);
			API.getResultArray();

			for (int i = 0; i < API.getResultArray().length(); i++) {
				String carNo = API.getResultArray().getJSONObject(i).getString("no");
				double carLat = API.getResultArray().getJSONObject(i).getDouble("lat");
				double carLng = API.getResultArray().getJSONObject(i).getDouble("lng");
				int carPower = API.getResultArray().getJSONObject(i).getInt("power");

				String carLatValue = String.valueOf(carLat);
				String carLngValue = String.valueOf(carLng);
				String carPowerValue = String.valueOf(carPower);

				double range = initialRange;
				double layoutY = visibleHeight - (carLat - (Lat - range)) / (2 * range) * visibleHeight;
				double layoutX = (carLng - (Lng - range)) / (2 * range) * visibleWidth;

				double dist = Math.pow((Math.pow((Lat - carLat), 2) + Math.pow((Lng - carLng), 2)), 0.5);
				String distValue = String.valueOf(Math.round(dist * 1e6) / 8);

				layoutX = Math.max(circleRadius, Math.min(layoutX, maxX));
				layoutY = Math.max(circleRadius, Math.min(layoutY, maxY));

				Circle circle = new Circle(circleRadius);
				if (carPower == 0) {
					circle.setFill(Color.TRANSPARENT);
					circle.setLayoutX(layoutX);
					circle.setLayoutY(layoutY);
				} else if (carPower <= 20) {
					circle.setFill(Color.RED);
					circle.setLayoutX(layoutX);
					circle.setLayoutY(layoutY);
				} else {
					circle.setFill(Color.BLUE);
					circle.setLayoutX(layoutX);
					circle.setLayoutY(layoutY);
				}

				circle.setOnMouseClicked(event -> {
					LabelCarNo.setText(carNo);
					LabelCarPower.setText(carPowerValue + " %");
					LabelCarDist.setText(distValue + " m");
					LabelCarLat.setText(carLatValue);
					LabelCarLng.setText(carLngValue);

				});

				circlePane.getChildren().add(circle);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		loadMapImage();
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
		addCars(Lat, Lng, initialRange);
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
		addCars(Lat, Lng, initialRange);
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
		addCars(Lat, Lng, initialRange);
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
		addCars(Lat, Lng, initialRange);

	}

	/**
	 * Event handler for zooming in on the map.
	 */
	@FXML
	private void zoomIn() {
		scaleFactor *= 1.1;
		double rng = initialRange;
		circlePane.setScaleX(scaleFactor);
		circlePane.setScaleY(scaleFactor);
		mapImageView.setScaleX(scaleFactor);
		mapImageView.setScaleY(scaleFactor);
		circleRadius = circleRadius / 1.1;
		loadMapImage();
		addCars(MapLat, MapLng, rng);
	}

	/**
	 * Event handler for zooming out on the map.
	 */
	@FXML
	private void zoomOut() {
		scaleFactor /= 1.1;
		double rng = initialRange;
		circlePane.setScaleX(scaleFactor);
		circlePane.setScaleY(scaleFactor);
		mapImageView.setScaleX(scaleFactor);
		mapImageView.setScaleY(scaleFactor);
		circleRadius = circleRadius * 1.1;
		loadMapImage();
		addCars(MapLat, MapLng, rng);
	}

	private Double[] newSite;

	public void rent(ActionEvent event) throws IOException {
		String rentCarNumber = LabelCarNo.getText();
		System.out.println(rentCarNumber);
		int resCode = API.renting(rentCarNumber);
		if (resCode == 200) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("successfullyRent.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} else if (resCode == 403) {
			System.out.println("already renting");
		} else if (resCode == 404) {
			System.out.println("car not found");
		} else if (resCode == 405) {
			System.out.println("car in use");
		} else
			System.out.println("error");
	}

	public Double[] getNewSite() {
		return newSite;
	}

	@FXML
	public void showCenterInfo(MouseEvent event) {
		double circleCenterX = centerPoint.getCenterX();
		double circleCenterY = centerPoint.getCenterY();
		double imagePointX = mapImageView.parentToLocal(centerPoint.localToParent(circleCenterX, circleCenterY)).getX();
		double imagePointY = mapImageView.parentToLocal(centerPoint.localToParent(circleCenterX, circleCenterY)).getY();
		System.out.println("X: " + imagePointX + ", Y: " + imagePointY);
	}

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
