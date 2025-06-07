package views.managersScenes;

import models.User;
import views.globalScenes.SideBarComponent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Dashboard scene for managers - shows overview stats and quick actions
 * @author dotac
 */
public class DashBoardScene {
    
    private Stage stage;
    private User currentUser;
    private BorderPane mainLayout;
    
    public DashBoardScene(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
        createDashboard();
    }
    
    private void createDashboard() {
        mainLayout = new BorderPane();
        
        // Create and add sidebar
        SideBarComponent sidebarComponent = new SideBarComponent(currentUser, stage);
        mainLayout.setLeft(sidebarComponent.getSidebar());
        
        // Create main content area
        VBox mainContent = createMainContent();
        mainLayout.setCenter(mainContent);
        
        // Set scene
        Scene scene = new Scene(mainLayout, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("POS Dashboard");
    }
    
    private VBox createMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #ecf0f1;");
        
        // Page title
        Label titleLabel = new Label("Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.DARKBLUE);
        
        // Stats cards row
        HBox statsRow = createStatsCards();
        
        // Quick actions section
        VBox quickActions = createQuickActionsSection();
        
        content.getChildren().addAll(titleLabel, statsRow, quickActions);
        return content;
    }
    
    private HBox createStatsCards() {
        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);
        
        // Inventory Stats Card
        VBox inventoryCard = createStatsCard(
            "📦 Inventory", 
            "8", 
            "Total Products", 
            "#3498db"
        );
 
        // Sales Stats Card
            VBox salesCard = createStatsCard(
                "💰 Sales Today", 
              "$1,250.00", 
              "Total Revenue", 
               "#2ecc71"
           );
               
         // Employee Stats Card
            VBox employeeCard = createStatsCard(
                "👥 Employees", 
              "8", 
              "Active Users", 
              "#e74c3c"
          );

        // total amount of items
        VBox totalItemCard = createStatsCard(
            "📦🥕️ In Stock", 
            "150", 
            "total items", 
            "#f39c12"
        );
        statsRow.getChildren().addAll(inventoryCard, totalItemCard);
        if (currentUser.isManager())
          statsRow.getChildren().addAll(salesCard, employeeCard);
        return statsRow;
    }
    
    private VBox createStatsCard(String title, String mainValue, String subtitle, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        card.setPrefHeight(120);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );
        
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
        
        // Add hover effect
        card.setOnMouseEntered(e -> 
            card.setStyle(
                "-fx-background-color: " + color + "; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);"
            )
        );
        
        card.setOnMouseExited(e -> 
            card.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
            )
        );
        
        return card;
    }
    
    private VBox createQuickActionsSection() {
        VBox quickActionsSection = new VBox(15);
        quickActionsSection.setPadding(new Insets(20, 0, 0, 0));
        
        Label sectionTitle = new Label("Quick Actions");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        sectionTitle.setTextFill(Color.DARKBLUE);
        
        HBox actionsRow = new HBox(15);
        actionsRow.setAlignment(Pos.CENTER_LEFT);
        
        // Quick action buttons
        VBox addProductAction = createQuickActionButton(
            "➕ Add Product", 
            "Quickly add new inventory", 
            "#27ae60"
        );
        
        VBox viewSalesAction = createQuickActionButton(
            "📊 View Sales", 
            "Check today's sales", 
            "#3498db"
        );
        
        VBox manageCashAction = createQuickActionButton(
            "💵 Check Cash", 
            "Review cash drawer", 
            "#f39c12"
        );
        
        VBox reportsAction = createQuickActionButton(
            "📈 Generate Report", 
            "Create sales report", 
            "#9b59b6"
        );
        
        actionsRow.getChildren().addAll(addProductAction, viewSalesAction, manageCashAction, reportsAction);
        quickActionsSection.getChildren().addAll(sectionTitle, actionsRow);
        
        return quickActionsSection;
    }
    
    private VBox createQuickActionButton(String title, String description, String color) {
        VBox actionButton = new VBox(8);
        actionButton.setPadding(new Insets(20));
        actionButton.setAlignment(Pos.CENTER_LEFT);
        actionButton.setPrefWidth(180);
        actionButton.setPrefHeight(80);
        actionButton.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: " + color + "; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 8; " +
            "-fx-cursor: hand;"
        );
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web(color));
        
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 11));
        descLabel.setTextFill(Color.GRAY);
        descLabel.setWrapText(true);
        
        actionButton.getChildren().addAll(titleLabel, descLabel);
        
        // Hover effects
        actionButton.setOnMouseEntered(e -> 
            actionButton.setStyle(
                "-fx-background-color: " + color + "; " +
                "-fx-background-radius: 8; " +
                "-fx-border-color: " + color + "; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 8; " +
                "-fx-cursor: hand;"
            )
        );
        
        actionButton.setOnMouseExited(e -> 
            actionButton.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 8; " +
                "-fx-border-color: " + color + "; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 8; " +
                "-fx-cursor: hand;"
            )
        );
        
        // TODO: Add click handlers for navigation
        actionButton.setOnMouseClicked(e -> {
            System.out.println("Clicked: " + title);
            // TODO: Implement navigation to respective scenes
        });
        
        return actionButton;
    }
    
    public void show() {
        stage.show();
    }
    
    // Method to refresh dashboard data (call this when returning to dashboard)
    public void refreshData() {
        // TODO: Implement data refresh logic
        // This would update the stats cards with fresh data from database
        System.out.println("Refreshing dashboard data...");
    }
}