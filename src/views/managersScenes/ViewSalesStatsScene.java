package views.managersScenes;

import controllers.StatisticsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import utils.CSVExporter;
import views.globalScenes.DashBoardScene;

import java.io.File;
import utils.ThemeManager;

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
        mainLayout.getStyleClass().add("main-layout");
        mainLayout.setPadding(new Insets(30));

        VBox content = new VBox(20);
        content.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Sales Statistics");
        title.getStyleClass().add("title-label");

        ComboBox<String> timeRangeCombo = new ComboBox<>();
        timeRangeCombo.getItems().addAll("Today", "This Week", "This Month", "This Year");
        timeRangeCombo.setValue("Today");
        timeRangeCombo.setPrefWidth(200);
        timeRangeCombo.getStyleClass().add("combo-box");

        TableView<StatisticsController.SaleSummary> statsTable = controller.createSalesSummaryTable();
        updateTable(statsTable, "Today");
        statsTable.getStyleClass().add("stats-table");

        Button exportButton = new Button("📁 Export to CSV");
        exportButton.getStyleClass().addAll("btn", "btn-primary");
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

        Button backButton = new Button("← Back");
        backButton.getStyleClass().addAll("btn", "btn-transparent");
        backButton.setOnAction(e -> {
            DashBoardScene dashboard = new DashBoardScene(primaryStage, currentUser);
            dashboard.show();
        });

        timeRangeCombo.setOnAction(e -> updateTable(statsTable, timeRangeCombo.getValue()));

        HBox header = new HBox(20, backButton, timeRangeCombo, exportButton);
        header.setAlignment(Pos.CENTER_LEFT);

        content.getChildren().addAll(title, header, statsTable);
        mainLayout.setCenter(content);

        Scene scene = new Scene(mainLayout, primaryStage.getWidth(), primaryStage.getHeight());
        ThemeManager.getInstance().registerScene(scene);
        return scene;
    }

    private void updateTable(TableView<StatisticsController.SaleSummary> table, String range) {
        table.getItems().clear();
        table.getItems().addAll(controller.getSalesSummary(range));
    }
}
