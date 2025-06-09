package views.globalScenes;

import controllers.DashboardController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import models.User;
import utils.ResHelper;
import views.cashierScenes.*;
import views.managersScenes.*;

public class DashBoardScene
{
    private Stage stage;
    private User currentUser;
    private DashboardController controller;
    private BorderPane mainLayout;
    private Scene scene;
    private VBox mainContent;
    private HBox statsRow;

    public DashBoardScene(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
        this.controller = new DashboardController();
        this.mainLayout = new BorderPane();
//        mainLayout.setMinSize(ResHelper.getHeight(),ResHelper.getWidth());
        this.scene = createDashboard();
    }

    private Scene createDashboard()
    {
        // Sidebar
        SideBarComponent sidebar = new SideBarComponent(currentUser, stage);
        mainLayout.setLeft(sidebar.getSidebar());

        // Main content
        mainContent = createMainContent();
        mainLayout.setCenter(mainContent);

        return new Scene(mainLayout,stage.getWidth(),stage.getHeight());
    }

    private VBox createMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #ecf0f1;");
        content.setFillWidth(true);

        Label titleLabel = new Label("Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.DARKBLUE);

        statsRow = createStatsCards();
        VBox quickActions = createQuickActionsSection();

        content.getChildren().addAll(titleLabel, statsRow, quickActions);
        return content;
    }

    private HBox createStatsCards()
    {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);

        row.getChildren().addAll(
            createStatsCard("📦 Inventory", String.valueOf(DashboardController.getTotalProducts()), "Total Products", "#3498db"),
            createStatsCard("📦🥕 In Stock", String.valueOf(DashboardController.getTotalItemsInStock()), "Total Items", "#f39c12")
        );

        if (currentUser.isManager()) {
            row.getChildren().addAll(
                createStatsCard("💰 Sales Today", "$" + String.format("%.2f", DashboardController.getTodaySalesTotal()), "Total Revenue", "#2ecc71"),
                createStatsCard("👥 Employees", String.valueOf(DashboardController.getActiveEmployees()), "Active Users", "#e74c3c")
            );
        }

        return row;
    }

    private VBox createStatsCard(String title, String mainValue, String subtitle, String color)
    {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(200, 120);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web(color));

        Label valueLabel = new Label(mainValue);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        valueLabel.setTextFill(Color.DARKSLATEGRAY);

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setFont(Font.font("Arial", 12));
        subtitleLabel.setTextFill(Color.GRAY);

        card.getChildren().addAll(titleLabel, valueLabel, subtitleLabel);

        card.setOnMouseEntered(e -> {
            titleLabel.setTextFill(Color.WHITE);
            valueLabel.setTextFill(Color.WHITE);
            subtitleLabel.setTextFill(Color.LIGHTGRAY);
            card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10;");
        });

        card.setOnMouseExited(e -> {
            titleLabel.setTextFill(Color.web(color));
            valueLabel.setTextFill(Color.DARKSLATEGRAY);
            subtitleLabel.setTextFill(Color.GRAY);
            card.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        });

        return card;
    }

    private VBox createQuickActionsSection()
    {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20, 0, 0, 0));

        Label sectionTitle = new Label("Quick Actions");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        sectionTitle.setTextFill(Color.DARKBLUE);

        HBox actions = new HBox(15);
        actions.setAlignment(Pos.CENTER_LEFT);

        if (User.Role.MANAGER == currentUser.getRole())
            actions.getChildren().addAll(
                createQuickActionButton("➕ Add Product", "Quickly add new inventory", "#27ae60"),
    //            createQuickActionButton("📊 View Sales", "Check today's sales", "#3498db"), I just realized this is literally the check cash, will keep removed if nothing breaks
                createQuickActionButton("📈 Generate Report", "Create sales report", "#9b59b6")
            );
        actions.getChildren().addAll(createQuickActionButton("💵 Check Cash", "Review cash drawer", "#f39c12"));
        section.getChildren().addAll(sectionTitle, actions);
        return section;
    }

    private VBox createQuickActionButton(String title, String description, String color) {
        VBox action = new VBox(8);
        action.setPadding(new Insets(20));
        action.setAlignment(Pos.CENTER_LEFT);
        action.setPrefSize(180, 80);
        action.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: " + color + "; -fx-border-width: 2;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web(color));

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 11));
        descLabel.setTextFill(Color.GRAY);
        descLabel.setWrapText(true);

        action.getChildren().addAll(titleLabel, descLabel);

        action.setOnMouseEntered(e -> {
            titleLabel.setTextFill(Color.BLACK);
            descLabel.setTextFill(Color.LIGHTGRAY);
            action.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 8;");
        });

        action.setOnMouseExited(e -> {
            titleLabel.setTextFill(Color.web(color));
            descLabel.setTextFill(Color.GRAY);
            action.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: " + color + "; -fx-border-width: 2;");
        });

        action.setOnMouseClicked(e -> {
            System.out.println("Clicked: " + title);
             if ("💵 Check Cash".equals(title)) {
                CheckCashScene checkCashScene = new CheckCashScene(stage, currentUser);
                stage.setScene(checkCashScene.getScene());
                stage.setTitle("Cash Register Check");
            }
             else if ("➕ Add Product".equals(title))
                new ManageProductsScene(stage, currentUser).show();
             else if ("📈 Generate Report".equals(title))
             {
                 stage.setScene(new ViewSalesStatsScene(stage, currentUser).getScene());
                 stage.setTitle("Sales report- quick action");
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
            mainContent.getChildren().add(1, statsRow);
        }
    }
}