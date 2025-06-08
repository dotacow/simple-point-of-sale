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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import views.globalScenes.DashBoardScene;

public class MakeSaleScene {
    private Stage primaryStage;
    private User currentUser;
    private DBHelper dbHelper;
    private VBox mainBox;
    private Label titleLabel;
    private ComboBox<String> productCombo;
    private TextField quantityField;
    private Button addButton;
    private TextArea cartArea;
    private Label totalLabel;
    private ComboBox<String> paymentCombo;
    private Button saleButton;
    private Button printButton;
    private Button clearButton;
    private Button backButton;
    private List<CartItem> cart;
    private double totalAmount = 0.0;
    private int lastSaleId = 0;

    public MakeSaleScene(Stage primaryStage, User currentUser) {
        this.primaryStage = primaryStage;
        this.currentUser = currentUser;
        this.dbHelper = new DBHelper();
        this.cart = new ArrayList<>();
        createUI();
        loadProducts();
    }

    private void createUI() {
        mainBox = new VBox();
        mainBox.setSpacing(15);
        mainBox.setStyle("-fx-background-color: #2c3e50; -fx-padding: 20;");
        titleLabel = new Label("Make Sale");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        Label productLabel = new Label("Select Product:");
        productLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        productCombo = new ComboBox<>();
        productCombo.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;");
        Label qtyLabel = new Label("Quantity:");
        qtyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        quantityField = new TextField("1");
        quantityField.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;");
        addButton = new Button("Add to Cart");
        addButton.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 15 20; " +
            "-fx-cursor: hand;"
        );
        addButton.setOnAction(e -> addToCart());
        Label cartLabel = new Label("Shopping Cart:");
        cartLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        cartArea = new TextArea();
        cartArea.setPrefHeight(150);
        cartArea.setEditable(false);
        cartArea.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;");
        totalLabel = new Label("Total: 0.00 JD");
        totalLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        Label paymentLabel = new Label("Payment Method:");
        paymentLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        paymentCombo = new ComboBox<>();
        paymentCombo.getItems().addAll("Cash", "Credit");
        paymentCombo.setValue("Cash");
        paymentCombo.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;");
        saleButton = new Button("Process Sale");
        saleButton.setStyle(
            "-fx-background-color: #2ecc71; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 10 20; " +
            "-fx-cursor: hand;" +
            "-fx-background-radius: 5;"
        );
        saleButton.setOnMouseEntered(e -> saleButton.setStyle(
            "-fx-background-color: #27ae60; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 10 20; " +
            "-fx-cursor: hand;" +
            "-fx-background-radius: 5;"
        ));
        saleButton.setOnMouseExited(e -> saleButton.setStyle(
            "-fx-background-color: #2ecc71; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 10 20; " +
            "-fx-cursor: hand;" +
            "-fx-background-radius: 5;"
        ));
        saleButton.setOnAction(e -> processSale());

        printButton = new Button("Print Invoice");
        printButton.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 15 20; " +
            "-fx-cursor: hand;"
        );
        printButton.setOnAction(e -> printInvoice());
        printButton.setDisable(true);

        clearButton = new Button("Clear Cart");
        clearButton.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 15 20; " +
            "-fx-cursor: hand;"
        );
        clearButton.setOnAction(e -> clearCart());

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
        buttonBox.getChildren().addAll(saleButton, printButton, clearButton, backButton);
        mainBox.getChildren().addAll(
            titleLabel, productLabel, productCombo, qtyLabel, quantityField, addButton,
            cartLabel, cartArea, totalLabel, paymentLabel, paymentCombo, buttonBox
        );
    }

    private void loadProducts() {
        try {
            Connection conn = dbHelper.connect();
            String query = "SELECT * FROM product WHERE StockQuantity > 0";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            productCombo.getItems().clear();
            
            while (rs.next()) {
                String item = rs.getInt("ProductId") + " - " + rs.getString("Name") + 
                             " (Stock: " + rs.getInt("StockQuantity") + 
                             ", Price: " + rs.getDouble("Price") + " JD)";
                productCombo.getItems().add(item);
            }
            conn.close();
        } catch (SQLException e) {
            showMessage("Error loading products: " + e.getMessage());
        }
    }

    private void addToCart() {
        String selectedProduct = productCombo.getValue();
        if (selectedProduct == null) {
            showMessage("Please select a product");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                showMessage("Quantity must be greater than 0");
                return;
            }
            String[] parts = selectedProduct.split(" - ");
            int productId = Integer.parseInt(parts[0]);
            String productName = parts[1].split(" \\(")[0];
            Connection conn = dbHelper.connect();
            String query = "SELECT * FROM product WHERE ProductId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                double price = rs.getDouble("Price");
                int stock = rs.getInt("StockQuantity");
                
                if (quantity > stock) {
                    showMessage("Not enough stock. Available: " + stock);
                    conn.close();
                    return;
                }
                CartItem item = new CartItem();
                item.productId = productId;
                item.productName = productName;
                item.quantity = quantity;
                item.price = price;
                item.total = price * quantity;
                cart.add(item);
                updateCartDisplay();
                quantityField.setText("1");
            }
            
            conn.close();
            
        } catch (NumberFormatException e) {
            showMessage("Please enter a valid quantity");
        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage());
        }
    }

    private void updateCartDisplay() {
        String cartText = "";
        totalAmount = 0.0;
        
        for (int i = 0; i < cart.size(); i++) {
            CartItem item = cart.get(i);
            cartText += (i + 1) + ". " + item.productName + " x" + item.quantity + 
                       " = " + String.format("%.2f", item.total) + " JD\n";
            totalAmount += item.total;
        }
        if (cartText.isEmpty()) {
            cartText = "Cart is empty";
        }
        cartArea.setText(cartText);
        totalLabel.setText("Total: " + String.format("%.2f", totalAmount) + " JD");
    }
    private void clearCart() {
        cart.clear();
        updateCartDisplay();
        printButton.setDisable(true);
    }
    private void processSale() {
        if (cart.isEmpty()) {
            showMessage("Cart is empty");
            return;
        }

        try {
            Connection conn = dbHelper.connect();
            
            String saleQuery = "INSERT INTO sale (UserId, CreatedAt, TotalPrice, PaymentMethod) VALUES (?, ?, ?, ?)";
            PreparedStatement saleStmt = conn.prepareStatement(saleQuery, Statement.RETURN_GENERATED_KEYS);
            saleStmt.setInt(1, currentUser.getId());
            saleStmt.setString(2, LocalDateTime.now().toString());
            saleStmt.setDouble(3, totalAmount);
            saleStmt.setString(4, paymentCombo.getValue());
            saleStmt.executeUpdate();
            ResultSet keys = saleStmt.getGeneratedKeys();
            if (keys.next()) {
                lastSaleId = keys.getInt(1);
            }
            for (CartItem item : cart) {
                String spQuery = "INSERT INTO saleproduct (SaleId, ProductId, Quantity) VALUES (?, ?, ?)";
                PreparedStatement spStmt = conn.prepareStatement(spQuery);
                spStmt.setInt(1, lastSaleId);
                spStmt.setInt(2, item.productId);
                spStmt.setInt(3, item.quantity);
                spStmt.executeUpdate();
                String updateQuery = "UPDATE product SET StockQuantity = StockQuantity - ? WHERE ProductId = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, item.quantity);
                updateStmt.setInt(2, item.productId);
                updateStmt.executeUpdate();
            }
            conn.close();
            showMessage("Sale processed successfully! Sale ID: " + lastSaleId);
            printButton.setDisable(false);
            cart.clear();
            updateCartDisplay();
            loadProducts();
        } catch (SQLException e) {
            showMessage("Error processing sale: " + e.getMessage());
        }
    }

    private void printInvoice() {
        if (lastSaleId == 0) {
            showMessage("No sale to print");
            return;
        }
        try {
            Connection conn = dbHelper.connect();
            
            String query = "SELECT s.*, u.Name FROM sale s JOIN user u ON s.UserId = u.UserId WHERE s.SaleId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, lastSaleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String invoice = "=== INVOICE ===\n";
                invoice += "Sale ID: " + rs.getInt("SaleId") + "\n";
                invoice += "Date: " + rs.getString("CreatedAt") + "\n";
                invoice += "Cashier: " + rs.getString("Name") + "\n";
                invoice += "Payment: " + rs.getString("PaymentMethod") + "\n";
                invoice += "Total: " + String.format("%.2f", rs.getDouble("TotalPrice")) + " JD\n";
                invoice += "===============\n";
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Invoice");
                alert.setHeaderText("Sale #" + lastSaleId);
                alert.setContentText(invoice);
                alert.showAndWait();
            }
            conn.close();
        } catch (SQLException e) {
            showMessage("Error printing invoice: " + e.getMessage());
        }
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void goBack() {
        System.out.println("Going back to main menu");
        DashBoardScene dashboard = new DashBoardScene(primaryStage, currentUser);
        dashboard.show();
    }

    public Scene getScene() {
        return new Scene(mainBox, 500, 700);
    }

    private static class CartItem {
        int productId;
        String productName;
        int quantity;
        double price;
        double total;
    }
}