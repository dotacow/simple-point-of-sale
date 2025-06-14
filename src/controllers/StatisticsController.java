package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Sale;
import utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StatisticsController {

    public static class SaleSummary {
        private String date;
        private int totalSales;
        private double totalRevenue;

        public SaleSummary(String date, int totalSales, double totalRevenue) {
            this.date = date;
            this.totalSales = totalSales;
            this.totalRevenue = totalRevenue;
        }

        public String getDate() {
            return date;
        }

        public int getTotalSales() {
            return totalSales;
        }

        public double getTotalRevenue() {
            return totalRevenue;
        }
    }

    public TableView<SaleSummary> createSalesSummaryTable() {
        TableView<SaleSummary> table = new TableView<>();

        TableColumn<SaleSummary, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setPrefWidth(150);

        TableColumn<SaleSummary, Integer> totalSalesCol = new TableColumn<>("Total Sales");
        totalSalesCol.setCellValueFactory(new PropertyValueFactory<>("totalSales"));
        totalSalesCol.setPrefWidth(150);

        TableColumn<SaleSummary, Double> revenueCol = new TableColumn<>("Total Revenue ($)");
        revenueCol.setCellValueFactory(new PropertyValueFactory<>("totalRevenue"));
        revenueCol.setPrefWidth(200);

        table.getColumns().addAll(dateCol, totalSalesCol, revenueCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }

    public ObservableList<SaleSummary> getSalesSummary(String range) {
        ObservableList<SaleSummary> summaries = FXCollections.observableArrayList();
        String query = "";
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        switch (range) {
            case "Today" -> {
                query = """
                        SELECT DATE(CreatedAt) AS day, COUNT(*) AS sales_count, SUM(TotalPrice) AS revenue
                        FROM sale
                        WHERE DATE(CreatedAt) = ?
                        GROUP BY day
                        ORDER BY day DESC;
                        """;
            }
            case "This Week" -> {
                query = """
                        SELECT DATE(CreatedAt) AS day, COUNT(*) AS sales_count, SUM(TotalPrice) AS revenue
                        FROM sale
                        WHERE CreatedAt >= DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)
                        GROUP BY day
                        ORDER BY day DESC;
                        """;
            }
            case "This Month" -> {
                query = """
                        SELECT DATE(CreatedAt) AS day, COUNT(*) AS sales_count, SUM(TotalPrice) AS revenue
                        FROM sale
                        WHERE YEAR(CreatedAt) = YEAR(CURDATE()) AND MONTH(CreatedAt) = MONTH(CURDATE())
                        GROUP BY day
                        ORDER BY day DESC;
                        """;
            }
            case "This Year" -> {
                query = """
                        SELECT DATE_FORMAT(CreatedAt, '%Y-%m') AS month, COUNT(*) AS sales_count, SUM(TotalPrice) AS revenue
                        FROM sale
                        WHERE YEAR(CreatedAt) = YEAR(CURDATE())
                        GROUP BY month
                        ORDER BY month DESC;
                        """;
            }
            default -> {
                return summaries;
            }
        }


        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if ("Today".equals(range)) {
                stmt.setString(1, now.format(formatter));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String label = rs.getString(1);
                int count = rs.getInt(2);
                double total = rs.getDouble(3);
                summaries.add(new SaleSummary(label, count, total));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return summaries;
    }
}
