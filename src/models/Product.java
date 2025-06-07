package models;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Product {
    
    public enum e_category {
        Pharmacy, Food, Hygiene
    }
    
    private int productId;
    private String name;
    private int quantity;
    private double price;
    private e_category category;
    private File image;

    public Product(int productId, String name, int quantity, double price, e_category category, File image) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.image = image;
    }

    // Getters here...
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public e_category getCategory() { return category; }
    public File getImage() { return image; }
    
        public byte[] getImageBytes() {
        if (image == null) return null;
        try (FileInputStream fis = new FileInputStream(image)) {
            return fis.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}