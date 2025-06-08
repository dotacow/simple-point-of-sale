package views.managersScenes;

import controllers.SaleController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Sale;
import models.User;
import views.globalScenes.SideBarComponent;

import java.sql.Timestamp;
import java.util.List;
import utils.ResHelper;

public class ManageSalesScene {

    private final SaleController controller = new SaleController();
    private final Stage stage;
    private final User currentUser;

    public ManageSalesScene(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
    }

    public void show() {
        BorderPane layout = new BorderPane();

        // Sidebar
        SideBarComponent sidebar = new SideBarComponent(currentUser, stage);
        layout.setLeft(sidebar.getSidebar());

        // Content
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        TableView<Sale> table = listSalesTable();
        loadSales(table);

        content.getChildren().add(table);
        layout.setCenter(content);

        stage.setScene(new Scene(layout,stage.getWidth(),stage.getHeight()));
        stage.setTitle("Manage Sales");
        stage.show();
    }

    private TableView<Sale> listSalesTable() {
        TableView<Sale> tableView = new TableView<>();

        TableColumn<Sale, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());

        TableColumn<Sale, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getUser().getName()));

        TableColumn<Sale, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getUser().getUsername()));

        TableColumn<Sale, Timestamp> timeCol = new TableColumn<>("Date");
        timeCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getCreatedAtTime()));

        TableColumn<Sale, Float> priceCol = new TableColumn<>("Total");
        priceCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleFloatProperty(cell.getValue().getTotalPrice()).asObject());

        TableColumn<Sale, String> methodCol = new TableColumn<>("Payment");
        methodCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPaymentMethod().name()));

        // Delete button column
        TableColumn<Sale, Void> deleteCol = new TableColumn<>("Delete");
        deleteCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");

            {
                deleteBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                deleteBtn.setOnAction(e -> {
                    Sale sale = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Confirm Deletion");
                    alert.setHeaderText("Are you sure you want to delete this sale?");
                    alert.setContentText("You take full responsibility in case of any legal issues related to this deletion.");

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            SaleController.deleteSale(sale.getId());
                            loadSales(getTableView());  // Refresh table
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteBtn);
                }
            }
        });

        tableView.getColumns().addAll(idCol, nameCol, usernameCol, timeCol, priceCol, methodCol, deleteCol);
        return tableView;
    }

    private void loadSales(TableView<Sale> table) {
        List<Sale> sales = controller.getAllSales(); // You'll implement this
        ObservableList<Sale> data = FXCollections.observableArrayList(sales);
        table.setItems(data);
    }
}
