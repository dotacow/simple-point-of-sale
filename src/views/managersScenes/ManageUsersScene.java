package views.managersScenes;

import controllers.UserController;
import models.User;
import models.User.Role;
import views.globalScenes.SideBarComponent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import utils.ResHelper;
import utils.ThemeManager;

public class ManageUsersScene {

    private final UserController controller = new UserController();

    private final User currentUser;
    private final Stage stage;

    public ManageUsersScene(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
    }

    public void show() {
        SideBarComponent sidebar = new SideBarComponent(currentUser, stage);

        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(20));
        mainContent.getStyleClass().add("manage-users-content");  // CSS class instead of inline bg color

        TableView<User> tableView = listUsersTable();

        Button addButton = new Button("➕ Add User");
        addButton.getStyleClass().add("btn-add-user");  // CSS class for button
        addButton.setOnAction(e -> showAddUserForm(tableView));

        VBox topPane = new VBox(10, addButton);
        topPane.setPadding(new Insets(0, 0, 10, 0));

        loadUsers(tableView);

        mainContent.getChildren().addAll(topPane, tableView);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setLeft(sidebar.getSidebar());
        mainLayout.setCenter(mainContent);
        mainLayout.setPadding(new Insets(10));

        Scene scene = new Scene(mainLayout, stage.getWidth(), stage.getHeight());
        ThemeManager.getInstance().registerScene(scene);

        stage.setTitle("Manage Users - " + currentUser.getName());
        stage.setScene(scene);
        stage.show();
    }

    private TableView<User> listUsersTable() {
        TableView<User> tableView = new TableView<>();
        tableView.getStyleClass().add("manage-users-table"); // Add table CSS class

        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getUsername()));

        TableColumn<User, Role> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getRole()));

        TableColumn<User, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.getStyleClass().add("btn-edit");
                deleteBtn.getStyleClass().add("btn-delete");

                editBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    showEditUserForm(user, getTableView());
                });

                deleteBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    try {
                        controller.deleteUser(user.getId());
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

        tableView.getColumns().addAll(idCol, nameCol, usernameCol, roleCol, actionsCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        return tableView;
    }

    private void loadUsers(TableView<User> tableView) {
        List<User> users = controller.getAllUsers();
        ObservableList<User> data = FXCollections.observableArrayList(users);
        tableView.setItems(data);
    }

    private void showAddUserForm(TableView<User> tableView) {
        Stage formStage = new Stage();
        formStage.setTitle("Add New User");

        TextField nameField = new TextField();
        TextField usernameField = new TextField();
        PasswordField pwdField = new PasswordField();

        ComboBox<Role> roleBox = new ComboBox<>();
        roleBox.getItems().setAll(Role.values());

        Button saveBtn = new Button("Save");
        saveBtn.getStyleClass().add("btn-save");
        saveBtn.setOnAction(e -> {
            try {
                String name = nameField.getText();
                String username = usernameField.getText();
                String password = pwdField.getText();
                Role role = roleBox.getValue();

                if (role == null) {
                    showAlert("Please select a role.");
                    return;
                }

                User newUser = new User(name, role, password, username);
                controller.addUser(newUser);
                loadUsers(tableView);
                formStage.close();

            } catch (SQLException sqlEx) {
                if (sqlEx.getMessage().contains("UNIQUE") || sqlEx.getMessage().contains("PRIMARY")) {
                    showAlert("User ID or Username already exists.");
                } else {
                    showAlert("Database error: " + sqlEx.getMessage());
                }
            } catch (Exception ex) {
                showAlert("Invalid input: " + ex.getMessage());
                ex.printStackTrace();
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
        Scene scene = new Scene(form, 300, 400);
        ThemeManager.getInstance().registerScene(scene);
        formStage.setScene(scene);
        formStage.show();
    }

    private void showEditUserForm(User user, TableView<User> tableView) {
        Stage formStage = new Stage();
        formStage.setTitle("Edit User");

        TextField nameField = new TextField(user.getName());
        TextField usernameField = new TextField(user.getUsername());
        PasswordField pwdField = new PasswordField();
        pwdField.setPromptText("Enter new password or leave blank to keep");

        ComboBox<Role> roleBox = new ComboBox<>();
        roleBox.getItems().setAll(Role.values());
        roleBox.setValue(user.getRole());

        Button saveBtn = new Button("Update");
        saveBtn.getStyleClass().add("btn-save");
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

                String passwordToSave = pwd.isEmpty() ? user.getPassword() : pwd;

                User updated = new User(user.getId(), name, role, passwordToSave, usrName);
                controller.updateUser(updated);

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
        Scene scene = new Scene(form, 300, 400);
        ThemeManager.getInstance().registerScene(scene);
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
