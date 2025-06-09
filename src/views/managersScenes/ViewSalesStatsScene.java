package views.managersScenes;

import controllers.StatisticsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import utils.CSVExporter;
import views.globalScenes.DashBoardScene;

import java.io.File;

public class ViewSalesStatsScene {

    private final Stage primaryStage;
    private final User currentUser;
    private final StatisticsController controller;

    public ViewSalesStatsScene(Stage stage, User currentUser) {
        this.primaryStage = stage;
        this.currentUser = currentUser;
        this.controller = new StatisticsController();
    }

    public Scene getScene() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #ecf0f1;");
        mainLayout.setPadding(new Insets(30));

        VBox content = new VBox(20);
        content.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Sales Statistics");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        title.setTextFill(Color.DARKBLUE);

        // Time range dropdown
        ComboBox<String> timeRangeCombo = new ComboBox<>();
        timeRangeCombo.getItems().addAll("Today", "This Week", "This Month", "This Year");
        timeRangeCombo.setValue("Today");
        timeRangeCombo.setPrefWidth(200);
        timeRangeCombo.setStyle("-fx-font-size: 14;");

        // Stats table
        TableView<StatisticsController.SaleSummary> statsTable = controller.createSalesSummaryTable();
        updateTable(statsTable, "Today");

        // Export button
        Button exportButton = new Button("📁 Export to CSV");
        exportButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        exportButton.setTextFill(Color.WHITE);
        exportButton.setStyle("-fx-background-color: #3498db; -fx-background-radius: 8;");
        exportButton.setOnMouseEntered(e -> exportButton.setStyle("-fx-background-color: #2980b9; -fx-background-radius: 8;"));
        exportButton.setOnMouseExited(e -> exportButton.setStyle("-fx-background-color: #3498db; -fx-background-radius: 8;"));
        exportButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Sales Report");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            fileChooser.setInitialFileName("sales_report.csv");
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                CSVExporter.exportSaleSummaries(statsTable.getItems(), file);
            }
        });

        // Back button
        Button backButton = new Button("← Back");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        backButton.setTextFill(Color.BLACK);
        backButton.setStyle("-fx-background-color: transparent;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #bdc3c7;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: transparent;"));
        backButton.setOnAction(e -> {
            DashBoardScene dashboard = new DashBoardScene(primaryStage, currentUser);
            dashboard.show();
        });

        // Handle range change
        timeRangeCombo.setOnAction(e -> updateTable(statsTable, timeRangeCombo.getValue()));

        HBox header = new HBox(20, backButton, timeRangeCombo, exportButton);
        header.setAlignment(Pos.CENTER_LEFT);

        content.getChildren().addAll(title, header, statsTable);
        mainLayout.setCenter(content);

        return new Scene(mainLayout, primaryStage.getWidth(), primaryStage.getHeight());
    }

    private void updateTable(TableView<StatisticsController.SaleSummary> table, String range) {
        table.getItems().clear();
        table.getItems().addAll(controller.getSalesSummary(range));
    }
}
