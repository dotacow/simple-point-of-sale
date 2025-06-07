package views.globalScenes;
import controllers.UserController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.User;
import views.cashierScenes.*;
import views.globalScenes.*;
import views.managersScenes.*;

public class LoginView {
    private Stage stage;
    private Scene scene;
    
    public LoginView(Stage stage)
    {
        this.stage = stage;
        createLoginScene();
    }
    
    private void createLoginScene() {
        // UI Elements
        Label titleLabel = new Label("Point of Sale System");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-text-fill: lightgray;");
        TextField usernameField = new TextField();
        styleTextField(usernameField);
        
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-text-fill: lightgray;");
        PasswordField passwordField = new PasswordField();
        styleTextField(passwordField);
        
        Button loginBtn = new Button("Login");
        styleLoginButton(loginBtn);
        
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");
        
        // Layout
        VBox layout = new VBox(15, titleLabel, usernameLabel, usernameField,
                passwordLabel, passwordField, loginBtn, messageLabel);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #2c3e50; -fx-font-size: 14px;");
        
        scene = new Scene(layout, 450,360);
        
        // Event Handling
        loginBtn.setOnAction(e -> handleLogin(usernameField, passwordField, messageLabel));
        
        // Allow Enter key to submit
        passwordField.setOnAction(e -> loginBtn.fire());
        usernameField.setOnAction(e -> passwordField.requestFocus());
    }
    
    private void handleLogin(TextField usernameField, PasswordField passwordField, Label messageLabel) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both username and password.");
            return;
        }
        
        User authenticatedUser = UserController.authenticate(username, password);
        if (authenticatedUser != null)
        {
            // Clear any error messages
            new DashBoardScene(stage, authenticatedUser).show();
            messageLabel.setText("");
        } else {
            messageLabel.setText("Invalid username or password.");
            // Clear password field for security
            passwordField.clear();
        }
    }
    
    public void show() {
        // Reset stage to windowed mode for login
        stage.setResizable(true);
        stage.setTitle("abu m7md's supermarket - Login");
        stage.setScene(scene);
        
        // Center the login window
        stage.centerOnScreen();
        stage.show();
    }
    
    private void styleTextField(TextField field) {
        field.setStyle(
            "-fx-background-color: #34495e;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: gray;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 10;"
        );
        field.setMaxWidth(250);
    }
    
    private void styleLoginButton(Button button) {
        button.setStyle(
            "-fx-background-color: #1abc9c;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #16a085;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        ));
        
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #1abc9c;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        ));
    }
}