package views.managersScenes;

import controllers.UserController;
import models.User;
import models.User.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class ManageUsersScene {

    private final UserController controller = new UserController();

    public void start(Stage stage, User user) {
        TableView<User> tableView = new TableView<>();

        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getUsrName()));

        TableColumn<User, Role> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getRole()));

        tableView.getColumns().addAll(idCol, nameCol, usernameCol, roleCol);

        // Add actions column with edit & delete buttons
        TableColumn<User, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("✏️");
            private final Button deleteBtn = new Button("🗑️");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;");
                deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                editBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    showEditUserForm(user, getTableView());
                });

                deleteBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    try {
                        controller.deleteUser(user.getId()); // Make sure this exists in UserController
                        getTableView().getItems().remove(user);
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

        tableView.getColumns().add(actionsCol);

        loadUsers(tableView);

        Button addButton = new Button("➕ Add User");
        addButton.setOnAction(e -> showAddUserForm(tableView));

        VBox topPane = new VBox(10, addButton);
        topPane.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(topPane);
        root.setCenter(tableView);
        root.setPadding(new Insets(10));

        stage.setTitle("User List");
        stage.setScene(new Scene(root, 700, 450));
        stage.show();
    }

    private void loadUsers(TableView<User> tableView) {
        List<User> users = controller.getAllUsers();
        ObservableList<User> data = FXCollections.observableArrayList(users);
        tableView.setItems(data);
    }

    private void showAddUserForm(TableView<User> tableView) {
        Stage formStage = new Stage();
        formStage.setTitle("Add New User");

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField usernameField = new TextField();
        PasswordField pwdField = new PasswordField();

        ComboBox<Role> roleBox = new ComboBox<>();
        roleBox.getItems().setAll(Role.values());

        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String usrName = usernameField.getText();
                String pwd = pwdField.getText();
                Role role = roleBox.getValue();

                if (role == null) {
                    showAlert("Please select a role.");
                    return;
                }

                User newUser = new User(id, name, role, pwd, usrName);
                try {
                    controller.addUser(newUser); // make sure this exists in UserController
                    loadUsers(tableView);
                    formStage.close();
                } catch (SQLException sqlEx) {
                    if (sqlEx.getMessage().contains("UNIQUE") || sqlEx.getMessage().contains("PRIMARY")) {
                        showAlert("User ID or Username already exists.");
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
                new Label("Username:"), usernameField,
                new Label("Password:"), pwdField,
                new Label("Role:"), roleBox,
                saveBtn
        );
        form.setPadding(new Insets(15));

        formStage.setScene(new Scene(form, 300, 400));
        formStage.show();
    }

    private void showEditUserForm(User user, TableView<User> tableView) {
        Stage formStage = new Stage();
        formStage.setTitle("Edit User");

        TextField nameField = new TextField(user.getName());
        TextField usernameField = new TextField(user.getUsrName());
        PasswordField pwdField = new PasswordField();
        pwdField.setPromptText("Enter new password or leave blank to keep");

        ComboBox<Role> roleBox = new ComboBox<>();
        roleBox.getItems().setAll(Role.values());
        roleBox.setValue(user.getRole());

        Button saveBtn = new Button("Update");
        saveBtn.setOnAction(e -> {
            try {
                String name = nameField.getText();
                String usrName = usernameField.getText();
                String pwd = pwdField.getText();
                Role role = roleBox.getValue();

                if (role == null) {
                    showAlert("Please select a role.");
                    return;
                }

                // If password is blank, keep existing one
                String passwordToSave = pwd.isEmpty() ? user.getPwd() : pwd;

                User updated = new User(user.getId(), name, role, passwordToSave, usrName);
                controller.updateUser(updated); // make sure this exists

                loadUsers(tableView);
                formStage.close();
            } catch (Exception ex) {
                showAlert("Update failed: " + ex.getMessage());
            }
        });

        VBox form = new VBox(10,
                new Label("Name:"), nameField,
                new Label("Username:"), usernameField,
                new Label("Password:"), pwdField,
                new Label("Role:"), roleBox,
                saveBtn
        );
        form.setPadding(new Insets(15));
        formStage.setScene(new Scene(form, 300, 400));
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
