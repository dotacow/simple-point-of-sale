package controllers;

import utils.DBHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardController {

    public static int getTotalProducts() {
        String query = "SELECT COUNT(*) FROM PRODUCT";
        return executeCountQuery(query);
    }

    public static int getTotalItemsInStock() {
        String query = "SELECT SUM(StockQuantity) FROM PRODUCT";
        return executeCountQuery(query);
    }

    public static double getTodaySalesTotal() {
        String query = "SELECT SUM(TotalPrice) FROM SALE WHERE DATE(CreatedAt) = CURRENT_DATE";
        return executeSumQuery(query);
    }

    public static int getActiveEmployees() {
        String query = "SELECT COUNT(*) FROM USER WHERE Role = 'Cashier' OR Role = 'Manager'";
        return executeCountQuery(query);
    }

    private static int executeCountQuery(String query) {
        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error executing count query: " + e.getMessage());
        }
        return 0;
    }

    private static double executeSumQuery(String query) {
        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error executing sum query: " + e.getMessage());
        }
        return 0.0;
    }
}