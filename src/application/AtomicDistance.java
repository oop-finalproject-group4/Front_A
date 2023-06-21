
package application;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONObject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.json.JSONObject;

public class AtomicDistance {

	private AtomicReference<Double> dis123 = new AtomicReference<>(0.0);
//	private AtomicReference<Integer> cha = new AtomicReference<>(0);
//	private AtomicReference<Double[]> site = new AtomicReference<>(null);
  

  public void addToSum() throws IOException {
	  
  	API.rentingStatus();
      JSONObject rentingStatus = API.getResultObject().getJSONObject("currentRenting");
      double start_lat = rentingStatus.getDouble("start_lat");
      double start_lng = rentingStatus.getDouble("start_lng");
//      double value = getDistance.nextDouble(); //取得新的距離
      System.out.println("start_lat :" + start_lat +", start_lng :"+ start_lng);
      data.newSite = distance.generate(start_lat, start_lng);
      data.lat = data.newSite[0];
      data.lng = data.newSite[1];
      System.out.println("newSite[0] :"+data.lat+", newSite[1] :"+data.lng);
      double dis = (double) API.getDistance(start_lat, start_lng, data.lat, data.lng);//距離
      API.rentingStatus();
		JSONObject rentingCarStatus = API.getResultObject().getJSONObject("currentCar");
//		data.charge = rentingCarStatus.getInt("power");
      double value = dis;
      Double oldValue;
      Double newValue;
      int cha = rentingCarStatus.getInt("power");
      if (data.charge - cha < 0) {
		  Stage popupStage = new Stage();
			popupStage.initModality(Modality.APPLICATION_MODAL);
			Label messageLabel = new Label("車子電力為零");
			messageLabel.setWrapText(true);
			messageLabel.setAlignment(Pos.CENTER);
			messageLabel.setTextAlignment(TextAlignment.CENTER);

			Button closeButton = new Button("確定");
			closeButton.setOnAction(e -> popupStage.close());

			VBox popupRoot = new VBox(messageLabel, closeButton);
			popupRoot.setAlignment(Pos.CENTER);
			popupRoot.setSpacing(10);
			popupRoot.setPadding(new Insets(20));

			Scene popupScene = new Scene(popupRoot, 250, 150);
			popupStage.setScene(popupScene);
			popupStage.showAndWait();
		  System.out.println("車子電力為零");

	  }
	  else if (0 < data.charge && data.charge < 25) {
		Stage popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		Label messageLabel = new Label(String.format("電量低於25%請儘速至充電站充電，否則電量低於20%將無法還車\n目前電量為：%s %", data.charge));
		messageLabel.setWrapText(true);
		messageLabel.setAlignment(Pos.CENTER);
		messageLabel.setTextAlignment(TextAlignment.CENTER);

		Button closeButton = new Button("確定");
		closeButton.setOnAction(e -> popupStage.close());

		VBox popupRoot = new VBox(messageLabel, closeButton);
		popupRoot.setAlignment(Pos.CENTER);
		popupRoot.setSpacing(10);
		popupRoot.setPadding(new Insets(20));

		Scene popupScene = new Scene(popupRoot, 250, 150);
		popupStage.setScene(popupScene);
		popupStage.showAndWait();
		
	  }else {
		  do {
	    	  oldValue = dis123.get(); //取得之前的dis
	          newValue = oldValue + value; //加上新的距離
	          cha = (int) (newValue/100);
	          data.charge -= cha;
	      } while (!dis123.compareAndSet(oldValue, newValue)); //照抄即可
	      System.out.println("New Dis: " + value);
	      System.out.println("Current Sum: " + newValue);
	      System.out.println("現在騎乘距離為 :" + dis123);
	      System.out.println("現在電量為 :" + data.charge);
	      
	  }
			}

  public double getDis() {
      return dis123.get();
  }

  
}