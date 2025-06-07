package views.globalScenes;

import controllers.UserController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.User;
import views.managersScenes.DashBoardScene;

public class LoginView {
    private Stage stage;

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
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
        layout.setStyle("-fx-font-size: 14px;");

        Scene scene = new Scene(layout, 300, 250);
        stage.setTitle("Login");
        stage.setScene(scene);

        // Event Handling
        loginBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            User authenticatedUser = UserController.authenticate(username, password);
            if (authenticatedUser != null) {
                messageLabel.setText("Login successful.");
                new DashBoardScene(stage, authenticatedUser).show();
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        });
    }
}
