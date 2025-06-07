package views.globalScenes;

import models.User;
import utils.SceneManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import views.managersScenes.*;
import views.cashierScenes.*;

/**
 * Sidebar navigation component for the POS system
 * @author dotac
 */
public class SideBarComponent {
    
    private VBox sidebar;
    private User currentUser;
    private Stage stage;
    private SceneManager sceneManager;
    
    public SideBarComponent(User currentUser, Stage stage) {
        this.currentUser = currentUser;
        this.stage = stage;
        this.sceneManager = SceneManager.getInstance();
        createSidebar();
    }

    private void createSidebar() {
        sidebar = new VBox();
        sidebar.setPrefWidth(250);
        sidebar.setMinWidth(250);
        sidebar.setMaxWidth(250);
        sidebar.setStyle("-fx-background-color: #2c3e50; -fx-padding: 20;");
        sidebar.setSpacing(5);
        
        // User info section
        createUserInfoSection();
        
        // Add separator
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #34495e;");
        sidebar.getChildren().add(separator);
        
        // Menu items based on user role
        createMenuItems();
        
        // Logout button at bottom
        createLogoutSection();
    }
    
    private void createUserInfoSection()
    {
        VBox userInfo = new VBox(5);
        userInfo.setPadding(new Insets(0, 0, 20, 0));
        userInfo.setAlignment(Pos.CENTER_LEFT);
    
        Label welcomeLabel = new Label("Welcome");
        welcomeLabel.setTextFill(Color.LIGHTGRAY);
        welcomeLabel.setFont(Font.font("Arial", 12));
        
        Label userNameLabel = new Label(currentUser.getName());
        userNameLabel.setTextFill(Color.WHITE);
        userNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        Label roleLabel = new Label(currentUser.getRole().toString());
        roleLabel.setTextFill(Color.LIGHTBLUE);
        roleLabel.setFont(Font.font("Arial", 12));
        
        userInfo.getChildren().addAll(welcomeLabel, userNameLabel, roleLabel);
        sidebar.getChildren().add(userInfo);
    }
    
    private void createMenuItems()
    {
        VBox menuSection = new VBox(2);
        menuSection.setAlignment(Pos.TOP_LEFT);
        
        // Manager-only menu items
        if (currentUser.getRole() == User.Role.MANAGER) {
            menuSection.getChildren().addAll(
                createMenuButton("📦 Manage Products", "Manage Products", this::navigateToManageProducts),
                createMenuButton("🛒 Manage Sales", "Manage Sales", this::navigateToManageSales),
                createMenuButton("📈 Sales Stats", "Sales Statistics", this::navigateToSalesStats),
                createMenuButton("👥 Manage Users", "Manage Users", this::navigateToManageUsers)
            );
        }
        
        // Common menu items for all users
        menuSection.getChildren().addAll(
            createMenuButton("📊 Dashboard", "Dashboard", this::navigateToDashboard),
            createMenuButton("💰 Make Sale", "Make Sale", this::navigateToMakeSale),
            createMenuButton("💵 Check Cash", "Check Cash", this::navigateToCheckCash)
        );
        
        sidebar.getChildren().add(menuSection);
    }
    
    private Button createMenuButton(String text, String tooltip, Runnable action) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 15 20; " +
            "-fx-cursor: hand;"
        );
        
        // Hover effects
        button.setOnMouseEntered(e -> 
            button.setStyle(
                "-fx-background-color: #34495e; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 15 20; " +
                "-fx-cursor: hand;"
            )
        );
        
        button.setOnMouseExited(e -> 
            button.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 15 20; " +
                "-fx-cursor: hand;"
            )
        );
        
        button.setOnAction(e -> action.run());
        
        return button;
    }
    
    private void createLogoutSection() {
        VBox logoutSection = new VBox();
        logoutSection.setPadding(new Insets(20, 0, 0, 0));
        logoutSection.setAlignment(Pos.BOTTOM_LEFT);
        
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #34495e;");
        
        Button logoutButton = createMenuButton("🚪 Logout", "Logout", this::logout);
        logoutButton.setStyle(
            "-fx-background-color: #e74c3c; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 15 20; " +
            "-fx-cursor: hand;"
        );
        
        logoutButton.setOnMouseEntered(e -> 
            logoutButton.setStyle(
                "-fx-background-color: #c0392b; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 15 20; " +
                "-fx-cursor: hand;"
            )
        );
        
        logoutButton.setOnMouseExited(e -> 
            logoutButton.setStyle(
                "-fx-background-color: #e74c3c; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 15 20; " +
                "-fx-cursor: hand;"
            )
        );
        
        logoutSection.getChildren().addAll(separator, logoutButton);
        sidebar.getChildren().add(logoutSection);
    }
    
    // Navigation methods - now using SceneManager
<<<<<<< HEAD
    private void navigateToDashboard() {
        System.out.println("Navigate to Dashboard");
        sceneManager.showDashboard();
=======
    private void navigateToDashboard()
    {
        System.out.println("Navigate to Dashboard");
        new DashBoardScene(stage, currentUser).show();
>>>>>>> dca527e75e7c842d83d862688c907656bdc50b65
    }
    
    private void navigateToManageProducts() {
        System.out.println("Navigate to Manage Products");
<<<<<<< HEAD
        sceneManager.showManageProducts();
=======
       new  ManageProductsScene(stage, currentUser).show();
>>>>>>> dca527e75e7c842d83d862688c907656bdc50b65
    }
    
    private void navigateToManageSales() {
        System.out.println("Navigate to Manage Sales");
<<<<<<< HEAD
//        sceneManager.showManageSales();
//todo:
=======
    //        sceneManager.showManageSales();
    //todo:
>>>>>>> dca527e75e7c842d83d862688c907656bdc50b65
    }
    
    private void navigateToSalesStats() {
        System.out.println("Navigate to Sales Stats");
<<<<<<< HEAD
//        sceneManager.showSalesStats();
=======
    //        sceneManager.showSalesStats();
>>>>>>> dca527e75e7c842d83d862688c907656bdc50b65
    }
    
    private void navigateToManageUsers()
    {
        System.out.println("Navigate to Manage Users");
<<<<<<< HEAD
//        sceneManager.showManageUsers();
=======
    //        sceneManager.showManageUsers();
>>>>>>> dca527e75e7c842d83d862688c907656bdc50b65
    }
    private void navigateToMakeSale()
    {
        System.out.println("Navigate to Make Sale");
<<<<<<< HEAD
//        sceneManager.showMakeSale();
=======
//            sceneManager.showMakeSale();
>>>>>>> dca527e75e7c842d83d862688c907656bdc50b65
    }
    
    private void navigateToCheckCash() {
        System.out.println("Navigate to Check Cash");
<<<<<<< HEAD
//        sceneManager.showCheckCash();
=======
    //        sceneManager.showCheckCash();
>>>>>>> dca527e75e7c842d83d862688c907656bdc50b65
    }
    
    private void logout()
    {
        System.out.println("Logout");
<<<<<<< HEAD
        sceneManager.logout();
=======
        new LoginView(stage).show();
>>>>>>> dca527e75e7c842d83d862688c907656bdc50b65
    }
    
    // Getter for the sidebar component
    public VBox getSidebar()
    {
        return sidebar;
    }
}