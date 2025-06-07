package views.managersScenes;

import controllers.ProductController;
import models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.sql.SQLException;

public class ManageProductsScene {

    private final ProductController controller = new ProductController();

    public void start(Stage stage) {
        TableView<Product> tableView = new TableView<>();

        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getProductId()).asObject());

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));

        TableColumn<Product, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getQuantity()).asObject());

        TableColumn<Product, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCategory().toString()));

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getPrice()).asObject());

        TableColumn<Product, ImageView> imageCol = new TableColumn<>("Image");
        imageCol.setCellValueFactory(cell -> {
            byte[] imgBytes = cell.getValue().getImageBytes();
            ImageView imageView = new ImageView();
            if (imgBytes != null && imgBytes.length > 0) {
                Image img = new Image(new ByteArrayInputStream(imgBytes), 50, 50, true, true);
                imageView.setImage(img);
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);
            }
            return new javafx.beans.property.SimpleObjectProperty<>(imageView);
        });

        tableView.getColumns().addAll(idCol, nameCol, qtyCol, catCol, priceCol, imageCol);

        loadProducts(tableView);

        Button addButton = new Button("➕ Add Product");
        addButton.setOnAction(e -> showAddProductForm(tableView));

        VBox topPane = new VBox(10, addButton);
        topPane.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(topPane);
        root.setCenter(tableView);
        root.setPadding(new Insets(10));

        stage.setTitle("Product List");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    private void loadProducts(TableView<Product> tableView) {
        List<Product> products = controller.getAllProducts();
        ObservableList<Product> data = FXCollections.observableArrayList(products);
        tableView.setItems(data);
    }

    private void showAddProductForm(TableView<Product> tableView) {
        Stage formStage = new Stage();
        formStage.setTitle("Add New Product");

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField qtyField = new TextField();
        TextField priceField = new TextField();

        ComboBox<Product.e_category> categoryBox = new ComboBox<>();
        categoryBox.getItems().setAll(Product.e_category.values());

        Button imageButton = new Button("Choose Image");
        final File[] selectedImage = new File[1];

        imageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Product Image");
            selectedImage[0] = fileChooser.showOpenDialog(formStage);
        });

        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                int qty = Integer.parseInt(qtyField.getText());
                double price = Double.parseDouble(priceField.getText());
                Product.e_category category = categoryBox.getValue();

                if (selectedImage[0] == null) {
                    showAlert("Please select an image.");
                    return;
                }

                Product newProduct = new Product(id, name, qty, category, selectedImage[0], price);

                try {
                    controller.addProduct(newProduct);
                    loadProducts(tableView);
                    formStage.close();
                } catch (SQLException sqlEx) {
                    if (sqlEx.getMessage().contains("UNIQUE") || sqlEx.getMessage().contains("PRIMARY")) {
                        showAlert("Product ID already exists. Please use a different ID.");
                    } else {
                        showAlert("Database error: " + sqlEx.getMessage());
                    }
                }

            } catch (Exception ex) {
                showAlert("Invalid input: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        VBox form = new VBox(10,
                new Label("ID:"), idField,
                new Label("Name:"), nameField,
                new Label("Quantity:"), qtyField,
                new Label("Price:"), priceField,
                new Label("Category:"), categoryBox,
                imageButton,
                saveBtn
        );
        form.setPadding(new Insets(15));

        Scene scene = new Scene(form, 300, 400);
        formStage.setScene(scene);
        formStage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
