package views.managersScenes;

import controllers.ProductController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Product;
import models.User;
import views.globalScenes.SideBarComponent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class ManageProductsScene {
    private final ProductController controller = new ProductController();
    private User currentUser;
    private Stage stage;

    public ManageProductsScene(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
    }

    public void show() {
        BorderPane mainLayout = new BorderPane();

        // Sidebar
        SideBarComponent sidebar = new SideBarComponent(currentUser, stage);
        mainLayout.setLeft(sidebar.getSidebar());

        // Main content (Product management interface)
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.setStyle("-fx-background-color: #ecf0f1;");

        TableView<Product> tableView = createProductTable();
        loadProducts(tableView);

        Button addButton = new Button("Add Product");
        addButton.setOnAction(e -> showAddProductForm(tableView));

        VBox topPane = new VBox(10, addButton);
        topPane.setPadding(new Insets(10));

        content.getChildren().addAll(topPane, tableView);
        mainLayout.setCenter(content);

        Scene scene = new Scene(mainLayout);
        stage.setScene(scene);
        stage.setTitle("Manage Products");
        stage.show();
    }

    private TableView<Product> createProductTable() {
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

        TableColumn<Product, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;");
                deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                editBtn.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    showEditProductForm(product, getTableView());
                });

                deleteBtn.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    try {
                        controller.deleteProduct(product.getProductId());
                        getTableView().getItems().remove(product);
                    } catch (SQLException ex) {
                        showAlert("Delete failed: " + ex.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        tableView.getColumns().addAll(idCol, nameCol, qtyCol, catCol, priceCol, imageCol, actionsCol);
        return tableView;
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
                controller.addProduct(newProduct);
                loadProducts(tableView);
                formStage.close();
            } catch (SQLException sqlEx) {
                if (sqlEx.getMessage().contains("UNIQUE") || sqlEx.getMessage().contains("PRIMARY")) {
                    showAlert("Product ID already exists. Please use a different ID.");
                } else {
                    showAlert("Database error: " + sqlEx.getMessage());
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

        Scene scene = new Scene(form, stage.getWidth(), stage.getHeight());
        formStage.setScene(scene);
        formStage.show();
    }

    private void showEditProductForm(Product product, TableView<Product> tableView) {
        Stage formStage = new Stage();
        formStage.setTitle("Edit Product");

        TextField nameField = new TextField(product.getName());
        TextField qtyField = new TextField(String.valueOf(product.getQuantity()));
        TextField priceField = new TextField(String.valueOf(product.getPrice()));

        ComboBox<Product.e_category> categoryBox = new ComboBox<>();
        categoryBox.getItems().setAll(Product.e_category.values());
        categoryBox.setValue(product.getCategory());

        Button saveBtn = new Button("Update");
        saveBtn.setOnAction(e -> {
            try {
                String name = nameField.getText();
                int qty = Integer.parseInt(qtyField.getText());
                double price = Double.parseDouble(priceField.getText());
                Product.e_category category = categoryBox.getValue();

                Product updated = new Product(product.getProductId(), name, qty, category, product.getImage(), price);
                controller.updateProduct(updated);
                loadProducts(tableView);
                formStage.close();
            } catch (Exception ex) {
                showAlert("Invalid input: " + ex.getMessage());
            }
        });

        VBox form = new VBox(10,
                new Label("Name:"), nameField,
                new Label("Quantity:"), qtyField,
                new Label("Price:"), priceField,
                new Label("Category:"), categoryBox,
                saveBtn
        );
        form.setPadding(new Insets(15));

        formStage.setScene(new Scene(form, 300, 350));
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
