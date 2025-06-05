/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.*;
import java.sql.*;
/**
 *
 * @author dotac
 */
public class Product {
    
    public enum e_category {
        PHARMACY,
        FOOD,
        HYGIENE
    }
    
    public void addProduct(int ProductId, String name, int quantity, e_category category, File image, double price) {
        String sql = "INSERT INTO `product`(`ProductId`, `Name`, `StockQuantity`, `Category`, `Image`, `Price`) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             FileInputStream fis = new FileInputStream(image)) {

            stmt.setInt(1, quantity);
            stmt.setString(2, name);
            stmt.setInt(3, quantity);
            stmt.setString(4, category.toString());
            stmt.setBinaryStream(5, fis, (int) image.length());
            stmt.setDouble(6, price);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Product inserted successfully.");
            }

        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }
}
