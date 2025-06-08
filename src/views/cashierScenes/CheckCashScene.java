/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.cashierScenes;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.User;
import utils.DBHelper;
import java.sql.*;
import views.globalScenes.DashBoardScene;

public class CheckCashScene {
    private Stage primaryStage;
    private User currentUser;
    private DBHelper dbHelper;
    private VBox mainBox;
    private Label titleLabel;
    private Label totalCashLabel;
    private Label exemptionLabel;
    private Label netCashLabel;
    private TextArea salesListArea;
    private Button refreshButton;
    private Button backButton;
    private double totalCash = 0.0;
    private static final double EXEMPTION_AMOUNT = 50.0;
    public CheckCashScene(Stage primaryStage, User currentUser) {
        this.primaryStage = primaryStage;
        this.currentUser = currentUser;
        this.dbHelper = new DBHelper();
        createUI();
        loadCashData();
    }
    private void createUI() {
        mainBox = new VBox();
        mainBox.setSpacing(20);
        mainBox.setStyle("-fx-background-color: #2c3e50; -fx-padding: 30;");
        titleLabel = new Label("Cash Register Check");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        Label salesLabel = new Label("Today's Cash Sales:");
        salesLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        salesListArea = new TextArea();
        salesListArea.setPrefHeight(200);
        salesListArea.setEditable(false);
        salesListArea.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;");
        totalCashLabel = new Label("Total Cash: 0.00 JD");
        totalCashLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        exemptionLabel = new Label("Exemption (First 50 JD): 0.00 JD");
        exemptionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        netCashLabel = new Label("Net Cash to Report: 0.00 JD");
        netCashLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
        refreshButton = new Button("Refresh");
        refreshButton.setStyle(
            "-fx-background-color: #3498db; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 10 20; " +
            "-fx-cursor: hand;" +
            "-fx-background-radius: 5;"
        );
        refreshButton.setOnMouseEntered(e -> refreshButton.setStyle(
            "-fx-background-color: #2980b9; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 10 20; " +
            "-fx-cursor: hand;" +
            "-fx-background-radius: 5;"
        ));
        refreshButton.setOnMouseExited(e -> refreshButton.setStyle(
            "-fx-background-color: #3498db; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 10 20; " +
            "-fx-cursor: hand;" +
            "-fx-background-radius: 5;"
        ));
        refreshButton.setOnAction(e -> {
            loadCashData();
            showMessage("Data refreshed!");
        });
        backButton = new Button("Back");
        backButton.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 15 20; " +
            "-fx-cursor: hand;"
        );
        backButton.setOnAction(e -> goBack());
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.getChildren().addAll(refreshButton, backButton);
        mainBox.getChildren().addAll(
            titleLabel, salesLabel, salesListArea, 
            totalCashLabel, exemptionLabel, netCashLabel, buttonBox
        );
    }

    private void loadCashData() {
        try {
            Connection conn = dbHelper.connect();
            String query = "SELECT * FROM sale WHERE PaymentMethod = 'Cash' AND DATE(CreatedAt) = CURDATE()";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            totalCash = 0.0;
            String salesText = "";
            while (rs.next()) {
                int saleId = rs.getInt("SaleId");
                double amount = rs.getDouble("TotalPrice");
                String time = rs.getString("CreatedAt");
                
                salesText += "Sale #" + saleId + " - " + amount + " JD - " + time + "\n";
                totalCash += amount;
            }
            if (salesText.isEmpty()) {
                salesText = "No cash sales today.";
            }
            salesListArea.setText(salesText);
            updateSummary();
            conn.close();
        } catch (SQLException e) {
            showMessage("Error loading data: " + e.getMessage());
        }
    }

    private void updateSummary() {
        totalCashLabel.setText("Total Cash: " + String.format("%.2f", totalCash) + " JD");
        double exemption = Math.min(totalCash, EXEMPTION_AMOUNT);
        exemptionLabel.setText("Exemption (First 50 JD): " + String.format("%.2f", exemption) + " JD");
        double netCash = Math.max(0, totalCash - EXEMPTION_AMOUNT);
        netCashLabel.setText("Net Cash to Report: " + String.format("%.2f", netCash) + " JD");
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void goBack() {
        System.out.println("Going back to dashboard");
        DashBoardScene dashboard = new DashBoardScene(primaryStage, currentUser);
        dashboard.show();
    }

    public Scene getScene() {
        return new Scene(mainBox, 600, 500);
    }
}
