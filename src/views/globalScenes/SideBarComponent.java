package views.globalScenes;

import models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.ThemeManager;
import views.cashierScenes.CheckCashScene;
import views.cashierScenes.MakeSaleScene;
import views.managersScenes.ManageProductsScene;
import views.managersScenes.ManageSalesScene;
import views.managersScenes.ManageUsersScene;
import views.managersScenes.ViewSalesStatsScene;

/**
 * Sidebar navigation component for the POS system
 */
public class SideBarComponent {

    private VBox sidebar;
    private User currentUser;
    private Stage stage;

    public SideBarComponent(User currentUser, Stage stage) {
        this.currentUser = currentUser;
        this.stage = stage;
        createSidebar();
        // Register sidebar for theme management
    }

    private void createSidebar() {
        sidebar = new VBox();
        sidebar.setPrefWidth(250);
        sidebar.setMinWidth(250);
        sidebar.setMaxWidth(250);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setSpacing(5);

        // User info section
        createUserInfoSection();

        // Add separator
        Separator separator = new Separator();
        separator.getStyleClass().add("sidebar-separator");
        sidebar.getChildren().add(separator);

        // Menu items based on user role
        createMenuItems();

        // Logout button at bottom
        createLogoutSection();
    }

    private void createUserInfoSection() {
        VBox userInfo = new VBox(5);
        userInfo.setPadding(new Insets(0, 0, 20, 0));
        userInfo.setAlignment(Pos.CENTER_LEFT);

        Label welcomeLabel = new Label("Welcome");
        welcomeLabel.getStyleClass().add("sidebar-welcome-label");

        Label userNameLabel = new Label(currentUser.getName());
        userNameLabel.getStyleClass().add("sidebar-username-label");

        Label roleLabel = new Label(currentUser.getRole().toString());
        roleLabel.getStyleClass().add("sidebar-role-label");

        userInfo.getChildren().addAll(welcomeLabel, userNameLabel, roleLabel);
        sidebar.getChildren().add(userInfo);
    }

    private void createMenuItems() {
        VBox menuSection = new VBox(2);
        menuSection.setAlignment(Pos.TOP_LEFT);

        // Manager-only menu items
        if (currentUser.getRole() == User.Role.MANAGER) {
            menuSection.getChildren().addAll(
                createMenuButton("📦 Manage Products", this::navigateToManageProducts),
                createMenuButton("🛒 Manage Sales", this::navigateToManageSales),
                createMenuButton("📈 Sales Stats", this::navigateToSalesStats),
                createMenuButton("👥 Manage Users", this::navigateToManageUsers)
            );
        }

        // Common menu items for all users
        menuSection.getChildren().addAll(
            createMenuButton("📊 Dashboard", this::navigateToDashboard),
            createMenuButton("💰 Make Sale", this::navigateToMakeSale),
            createMenuButton("💵 Check Cash", this::navigateToCheckCash)
        );

        sidebar.getChildren().add(menuSection);
    }

    private Button createMenuButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().add("sidebar-menu-button");
        button.setOnAction(e -> action.run());
        return button;
    }

    private void createLogoutSection() {
        VBox logoutSection = new VBox();
        logoutSection.setPadding(new Insets(20, 0, 0, 0));
        logoutSection.setAlignment(Pos.BOTTOM_LEFT);

        Separator separator = new Separator();
        separator.getStyleClass().add("sidebar-separator");

        Button logoutButton = createMenuButton("🚪 Logout", this::logout);
        logoutButton.getStyleClass().add("sidebar-logout-button");

        logoutSection.getChildren().addAll(separator, logoutButton);
        sidebar.getChildren().add(logoutSection);
    }

    // Navigation methods
    private void navigateToDashboard() {
        new DashBoardScene(stage, currentUser).show();
    }

    private void navigateToManageProducts() {
        new ManageProductsScene(stage, currentUser).show();
    }

    private void navigateToManageSales() {
        new ManageSalesScene(stage, currentUser).show();
    }

    private void navigateToSalesStats() {
        stage.setScene(new ViewSalesStatsScene(stage, currentUser).getScene());
        stage.setTitle("Sale reports");
        stage.show();
    }

    private void navigateToManageUsers() {
        new ManageUsersScene(stage, currentUser).show();
    }

    private void navigateToMakeSale() {
        stage.setScene(new MakeSaleScene(stage, currentUser).getScene());
        stage.setTitle("Process New Sale");
        stage.show();
    }

    private void navigateToCheckCash() {
        stage.setScene(new CheckCashScene(stage, currentUser).getScene());
        stage.setTitle("Cash Register Check");
        stage.show();
    }

    private void logout() {
        new LoginView(stage).show();
    }

    public VBox getSidebar() {
        return sidebar;
    }
}
