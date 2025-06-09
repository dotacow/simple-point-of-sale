package views.cashierScenes;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.User;
import utils.DBHelper;
import utils.ThemeManager;
import views.globalScenes.DashBoardScene;

import java.sql.*;

public class CheckCashScene {
    private static final double EXEMPTION_AMOUNT = 50.0;

    private final Stage primaryStage;
    private final User currentUser;
    private final DBHelper dbHelper;

    private VBox mainBox;
    private TextArea salesListArea;
    private Label totalCashLabel, exemptionLabel, netCashLabel;
    private double totalCash;

    public CheckCashScene(Stage primaryStage, User currentUser) {
        this.primaryStage = primaryStage;
        this.currentUser = currentUser;
        this.dbHelper = new DBHelper();
        createUI();
        loadCashData();
    }

    private void createUI() {
        mainBox = new VBox(20);
        mainBox.setPadding(new Insets(30));
        mainBox.getStyleClass().add("cash-register-container");

        Label titleLabel = new Label("Cash Register Check");
        titleLabel.getStyleClass().add("cash-register-title");

        Label salesLabel = new Label("Today's Cash Sales:");
        salesLabel.getStyleClass().add("cash-register-subtitle");

        salesListArea = new TextArea();
        salesListArea.setPrefHeight(200);
        salesListArea.setEditable(false);
        salesListArea.getStyleClass().add("cash-register-textarea");

        totalCashLabel = new Label("Total Cash: 0.00 JD");
        exemptionLabel = new Label("Exemption (First 50 JD): 0.00 JD");
        netCashLabel = new Label("Net Cash to Report: 0.00 JD");

        totalCashLabel.getStyleClass().add("cash-register-summary");
        exemptionLabel.getStyleClass().add("cash-register-summary");
        netCashLabel.getStyleClass().add("cash-register-net");

        Button refreshButton = new Button("Refresh");
        refreshButton.getStyleClass().add("cash-register-refresh-btn");
        refreshButton.setOnAction(e -> {
            loadCashData();
            showMessage("Data refreshed!");
        });

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("cash-register-back-btn");
        backButton.setOnAction(e -> goBack());

        HBox buttonBox = new HBox(10, refreshButton, backButton);

        mainBox.getChildren().addAll(
            titleLabel, salesLabel, salesListArea,
            totalCashLabel, exemptionLabel, netCashLabel, buttonBox
        );
    }

    private void loadCashData() {
        StringBuilder salesText = new StringBuilder();
        totalCash = 0.0;

        try (Connection conn = dbHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT * FROM sale WHERE PaymentMethod = 'Cash' AND DATE(CreatedAt) = CURDATE()")) {

            while (rs.next()) {
                int saleId = rs.getInt("SaleId");
                double amount = rs.getDouble("TotalPrice");
                String time = rs.getString("CreatedAt");

                salesText.append(String.format("Sale #%d - %.2f JD - %s%n", saleId, amount, time));
                totalCash += amount;
            }

            if (salesText.length() == 0) {
                salesText.append("No cash sales today.");
            }

            salesListArea.setText(salesText.toString());
            updateSummary();

        } catch (SQLException e) {
            showMessage("Error loading data: " + e.getMessage());
        }
    }

    private void updateSummary() {
        double exemption = Math.min(totalCash, EXEMPTION_AMOUNT);
        double netCash = Math.max(0, totalCash - EXEMPTION_AMOUNT);

        totalCashLabel.setText(String.format("Total Cash: %.2f JD", totalCash));
        exemptionLabel.setText(String.format("Exemption (First 50 JD): %.2f JD", exemption));
        netCashLabel.setText(String.format("Net Cash to Report: %.2f JD", netCash));
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void goBack() {
        new DashBoardScene(primaryStage, currentUser).show();
    }

    public Scene getScene() {
        Scene scene = new Scene(mainBox, primaryStage.getWidth(), primaryStage.getHeight());
        ThemeManager.getInstance().registerScene(scene);
        return scene;
    }
}
