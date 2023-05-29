package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Registration extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("電動車租借平台");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER); // Center align the content
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Title
        Label titleLabel = new Label("電動車租借平台");
        titleLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");
        gridPane.add(titleLabel, 0,0,2,1); // Span two columns

        // Account
        Label accountLabel = new Label("電子信箱:");
        TextField accountField = new TextField();
        gridPane.add(accountLabel, 0, 1);
        gridPane.add(accountField, 1, 1);

        // Password
        Label passwordLabel = new Label("密碼:");
        PasswordField passwordField = new PasswordField();
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2);

        // Register Button
        Button registerButton = new Button("註冊");
        Button loginButton = new Button("登入");
        registerButton.setOnAction(click -> openRegistrationForm());
        HBox buttonBox = new HBox(20,registerButton,loginButton);
        buttonBox.setAlignment(Pos.CENTER); // Center align the button
        gridPane.add(buttonBox, 0, 3, 2, 1); // Span two columns

        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);

        // Set the stage to full screen
       Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
       primaryStage.setX(screenBounds.getMinX());
       primaryStage.setY(screenBounds.getMinY());
       primaryStage.setWidth(screenBounds.getWidth());
       primaryStage.setHeight(screenBounds.getHeight());

        primaryStage.show();
    }

    private void openRegistrationForm() {
   
        Stage registrationStage = new Stage();
        registrationStage.setTitle("Registration Form");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER); // Center align the content
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        
        //account
        Label nameLabel = new Label("電子信箱:");
        TextField nameField = new TextField();
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameField, 1, 0);

        // Password
        Label passwordLabel = new Label("密碼:");
        TextField passwordField = new TextField();
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        
        // phone number
        Label phoneLabel = new Label("手機號碼:");
        TextField phoneField = new TextField();
        gridPane.add(phoneLabel, 0, 2);
        gridPane.add(phoneField, 1, 2);
        
        // Credit Card Number
        Label ccLabel = new Label("信用卡卡號:");
        TextField ccField = new TextField();
        gridPane.add(ccLabel, 0, 3);
        gridPane.add(ccField, 1, 3);

        // Buttons
        Button confirm = new Button("確認");;
        HBox buttonBox = new HBox(20,confirm);
        
        
        confirm.setOnAction(event -> {
            registrationStage.close(); // Close the registration form window
        });
       
        
        buttonBox.setAlignment(Pos.CENTER); // Center align the buttons
        gridPane.add(buttonBox, 0, 4, 2, 1); // Span two columns

        Scene scene = new Scene(gridPane);
        registrationStage.setScene(scene);

        // Set the stage to full screen
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        registrationStage.setX(screenBounds.getMinX());
        registrationStage.setY(screenBounds.getMinY());
        registrationStage.setWidth(screenBounds.getWidth());
        registrationStage.setHeight(screenBounds.getHeight());

        registrationStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}