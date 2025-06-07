package controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import models.Product;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductController {

    public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO `product`(`ProductId`, `Name`, `StockQuantity`, `Category`, `Image`, `Price`) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = utils.DBHelper.connect(); PreparedStatement stmt = conn.prepareStatement(sql); FileInputStream fis = new FileInputStream(product.getImage())) {

            stmt.setInt(1, product.getProductId());
            stmt.setString(2, product.getName());
            stmt.setInt(3, product.getQuantity());
            stmt.setString(4, product.getCategory().toString());
            stmt.setBinaryStream(5, fis, (int) product.getImage().length());
            stmt.setDouble(6, product.getPrice());

            stmt.executeUpdate();
            System.out.println("Product inserted.");

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || e.getMessage().toLowerCase().contains("duplicate")) {  // MySQL duplicate key error
                throw new RuntimeException("A product with this ID already exists.");
            } else {
                throw new RuntimeException("Database error: " + e.getMessage(), e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM product";

        try (Connection conn = utils.DBHelper.connect(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("ProductId");
                String name = rs.getString("Name");
                int quantity = rs.getInt("StockQuantity");
                String categoryStr = rs.getString("Category");
                byte[] imageBytes = rs.getBytes("Image");
                double price = rs.getDouble("Price");

                Product.e_category category = Product.e_category.valueOf(categoryStr);

                // Convert byte[] to File
                File tempImage = null;
                if (imageBytes != null) {
                    tempImage = File.createTempFile("product_" + id + "_", ".img");
                    try (FileOutputStream fos = new FileOutputStream(tempImage)) {
                        fos.write(imageBytes);
                    }
                    tempImage.deleteOnExit(); // optional: delete temp file when app exits
                }

                Product product = new Product(id, name, quantity, category, tempImage, price);
                productList.add(product);
            }

        } catch (SQLException e) {
            System.err.println("Database error while fetching products: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("File I/O error: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid category in database: " + e.getMessage());
        }

        return productList;
    }

    public void updateProduct(Product product) throws SQLException {
        String sql = "UPDATE `product` SET `Name`=?, `StockQuantity`=?, `Category`=?, `Image`=?, `Price`=? WHERE `ProductId`=?";

        try (Connection conn = utils.DBHelper.connect(); PreparedStatement stmt = conn.prepareStatement(sql); FileInputStream fis = new FileInputStream(product.getImage())) {

            stmt.setString(1, product.getName());
            stmt.setInt(2, product.getQuantity());
            stmt.setString(3, product.getCategory().toString());
            stmt.setBinaryStream(4, fis, (int) product.getImage().length());
            stmt.setDouble(5, product.getPrice());
            stmt.setInt(6, product.getProductId());

            stmt.executeUpdate();
            System.out.println("Product updated.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(int productId) throws SQLException {
        String sql = "DELETE FROM `product` WHERE `ProductId`=?";

        try (Connection conn = utils.DBHelper.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            stmt.executeUpdate();
            System.out.println("Product deleted.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
