package views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import models.Product;
import controllers.*;

import java.io.File;

public class ProductView {

    private File selectedImage;

    public void start(Stage primaryStage) {
        // UI Controls
        TextField txtId = new TextField();
        txtId.setPromptText("Product ID");

        TextField txtName = new TextField();
        txtName.setPromptText("Product Name");

        TextField txtQuantity = new TextField();
        txtQuantity.setPromptText("Quantity");

        TextField txtPrice = new TextField();
        txtPrice.setPromptText("Price");

        ComboBox<Product.e_category> cmbCategory = new ComboBox<>();
        cmbCategory.getItems().addAll(Product.e_category.values());

        Button btnSelectImage = new Button("Select Image");
        Button btnAdd = new Button("Add Product");

        Label lblStatus = new Label();

        // Event: Select Image
        btnSelectImage.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Product Image");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg")
            );
            selectedImage = fileChooser.showOpenDialog(primaryStage);
        });

        // Event: Add Product
        btnAdd.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                String name = txtName.getText();
                int quantity = Integer.parseInt(txtQuantity.getText());
                double price = Double.parseDouble(txtPrice.getText());
                Product.e_category category = cmbCategory.getValue();

                if (selectedImage == null || category == null) {
                    lblStatus.setText("⚠ Please select image and category.");
                    return;
                }

                Product product = new Product(id, name, quantity, price, category, selectedImage);
                ProductController controller = new ProductController();
                controller.addProduct(product);

                lblStatus.setText("✅ Product added.");

                // Clear fields
                txtId.clear();
                txtName.clear();
                txtQuantity.clear();
                txtPrice.clear();
                cmbCategory.setValue(null);
                selectedImage = null;

            } catch (NumberFormatException ex) {
                lblStatus.setText("❌ Invalid number format.");
            } catch (Exception ex) {
                ex.printStackTrace();
                lblStatus.setText("❌ Error adding product.");
            }
        });

        VBox layout = new VBox(10,
                new Label("Add New Product"),
                txtId,
                txtName,
                txtQuantity,
                txtPrice,
                cmbCategory,
                btnSelectImage,
                btnAdd,
                lblStatus
        );
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 400, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Product View - Add Product");
        primaryStage.show();
    }
}
