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
import java.util.List;

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

        BorderPane root = new BorderPane(tableView);
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
}
