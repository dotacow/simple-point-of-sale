package views.globalScenes;

import controllers.DashboardController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.User;
import utils.ThemeManager;
import views.cashierScenes.CheckCashScene;
import views.managersScenes.ManageProductsScene;
import views.managersScenes.ViewSalesStatsScene;

public class DashBoardScene {
    private Stage stage;
    private User currentUser;
    private DashboardController controller;
    private BorderPane mainLayout;
    private Scene scene;
    private VBox mainContent;
    private HBox statsRow;
    private ThemeManager themeManager;

    public DashBoardScene(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
        this.controller = new DashboardController();
        this.themeManager = ThemeManager.getInstance();
        this.mainLayout = new BorderPane();
        this.scene = createDashboard();

        // Register this scene with the theme manager
        themeManager.registerScene(scene);
    }

    private Scene createDashboard() {
        SideBarComponent sidebar = new SideBarComponent(currentUser, stage);
        mainLayout.setLeft(sidebar.getSidebar());

        mainContent = createMainContent();
        mainLayout.setCenter(mainContent);

        return new Scene(mainLayout, stage.getWidth(), stage.getHeight());
    }

    private VBox createMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setFillWidth(true);
        content.getStyleClass().add("dashboard-content");

        ComboBox<String> themeSwitcher = createThemeSwitcher();

        Label titleLabel = new Label("Dashboard");
        titleLabel.getStyleClass().add("dashboard-title");

        statsRow = createStatsCards();
        VBox quickActions = createQuickActionsSection();

        content.getChildren().addAll(themeSwitcher, titleLabel, statsRow, quickActions);
        return content;
    }

    private ComboBox<String> createThemeSwitcher() {
        ComboBox<String> themeBox = new ComboBox<>();
        themeBox.getItems().addAll("Light", "Dark", "Gruvbox");
        
        // Set current theme as selected
        String currentTheme = themeManager.getCurrentTheme();
        themeBox.setValue(currentTheme.substring(0, 1).toUpperCase() + currentTheme.substring(1));
        themeBox.getStyleClass().add("theme-switcher");

        themeBox.setOnAction(e -> {
            String selected = themeBox.getValue().toLowerCase();
            // Change theme globally for all registered scenes
            themeManager.changeTheme(selected);
        });

        return themeBox;
    }

    private HBox createStatsCards() {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);

        row.getChildren().addAll(
            createStatsCard("📦 Inventory", String.valueOf(DashboardController.getTotalProducts()), "Total Products", "inventory"),
            createStatsCard("📦🥕 In Stock", String.valueOf(DashboardController.getTotalItemsInStock()), "Total Items", "stock")
        );

        if (currentUser.isManager()) {
            row.getChildren().addAll(
                createStatsCard("💰 Sales Today", "$" + String.format("%.2f", DashboardController.getTodaySalesTotal()), "Total Revenue", "sales"),
                createStatsCard("👥 Employees", String.valueOf(DashboardController.getActiveEmployees()), "Active Users", "employees")
            );
        }

        return row;
    }

    private VBox createStatsCard(String title, String mainValue, String subtitle, String type) {
        VBox card = new VBox(10);
        card.getStyleClass().addAll("stats-card", "stats-card-" + type);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("stats-card-title");

        Label valueLabel = new Label(mainValue);
        valueLabel.getStyleClass().add("stats-card-value");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.getStyleClass().add("stats-card-subtitle");

        card.getChildren().addAll(titleLabel, valueLabel, subtitleLabel);
        return card;
    }

    private VBox createQuickActionsSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20, 0, 0, 0));

        Label sectionTitle = new Label("Quick Actions");
        sectionTitle.getStyleClass().add("section-title");

        HBox actions = new HBox(15);
        actions.setAlignment(Pos.CENTER_LEFT);

        if (User.Role.MANAGER == currentUser.getRole()) {
            actions.getChildren().addAll(
                createQuickActionButton("➕ Add Product", "Quickly add new inventory", "add-product"),
                createQuickActionButton("📈 Generate Report", "Create sales report", "generate-report")
            );
        }

        actions.getChildren().add(
            createQuickActionButton("💵 Check Cash", "Review cash drawer", "check-cash")
        );

        section.getChildren().addAll(sectionTitle, actions);
        return section;
    }

    private VBox createQuickActionButton(String title, String description, String type) {
        VBox action = new VBox(8);
        action.getStyleClass().addAll("quick-action", "quick-action-" + type);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("quick-action-title");

        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("quick-action-desc");

        action.getChildren().addAll(titleLabel, descLabel);

        action.setOnMouseClicked(e -> {
            if ("💵 Check Cash".equals(title)) {
                CheckCashScene checkCashScene = new CheckCashScene(stage, currentUser);
                stage.setScene(checkCashScene.getScene());
                stage.setTitle("Cash Register Check");
            } else if ("➕ Add Product".equals(title)) {
                new ManageProductsScene(stage, currentUser).show();
            } else if ("📈 Generate Report".equals(title)) {
                stage.setScene(new ViewSalesStatsScene(stage, currentUser).getScene());
                stage.setTitle("Sales report - quick action");
                stage.show();
            }
        });

        return action;
    }

    public void show() {
        stage.setTitle("Dashboard - " + currentUser.getName());
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public void refreshData() {
        if (statsRow != null && mainContent != null) {
            mainContent.getChildren().remove(statsRow);
            statsRow = createStatsCards();
            mainContent.getChildren().add(2, statsRow);
        }
    }
}