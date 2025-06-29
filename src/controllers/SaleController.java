package controllers;

import models.Sale;
import utils.DBHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
EXAMPLE USAGE:
Sale sale = new Sale(1, 49.99f, Sale.PaymentMethod.CASH);
SaleController.addSale(sale);
*/
public class SaleController {

    public static void addSale(Sale sale) {
        String sql = "INSERT INTO sale (userId, createdAt, totalPrice, paymentMethod) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sale.getUserId());
            stmt.setTimestamp(2, sale.getCreatedAtTime());
            stmt.setFloat(3, sale.getTotalPrice());
            stmt.setString(4, sale.getPaymentMethod().name());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteSale(int id) {
        String sql = "DELETE FROM sale WHERE saleId = ?";
        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Sale getSaleById(int id) {
        String sql = "SELECT * FROM sale WHERE saleId = ?";
        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Sale(
                    rs.getInt("saleId"),
                    rs.getInt("userId"),
                    rs.getTimestamp("createdAt"),
                    rs.getFloat("totalPrice"),
                    Sale.PaymentMethod.valueOf(rs.getString("paymentMethod").toUpperCase())
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM sale";

        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                sales.add(new Sale(
                    rs.getInt("saleId"),
                    rs.getInt("userId"),
                    rs.getTimestamp("createdAt"),
                    rs.getFloat("totalPrice"),
                    Sale.PaymentMethod.valueOf(rs.getString("paymentMethod").toUpperCase())
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sales;
    }
}
