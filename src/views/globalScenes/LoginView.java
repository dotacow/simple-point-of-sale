package views.globalScenes;

import controllers.UserController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView {

    public void start(Stage stage) {
        // UI Elements
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginBtn = new Button("Login");
        Label messageLabel = new Label();
        // Layout
        VBox layout = new VBox(10, usernameLabel, usernameField,
                                    passwordLabel, passwordField,
                                    loginBtn, messageLabel);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-font-size: 14px;");//TODO: add global styling elements

        Scene scene = new Scene(layout, 300, 250);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
         // Event Handling
        loginBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            boolean success = UserController.authenticate(username, password);
            if (success) {
                messageLabel.setText("Login successful.");
                // TODO: Navigate to dashboard
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        });
    }
}
