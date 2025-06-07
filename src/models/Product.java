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

    // For creating new products
    public Product(int id, String name, int qty, e_category category, File imageFile, double price) {
        this.productId = id;
        this.name = name;
        this.quantity = qty;
        this.category = category;
        this.image = imageFile;
        this.price = price;
    }
    
    

    // Getters here...
    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public e_category getCategory() {
        return category;
    }

    public File getImage() {
        return image;
    }

    public byte[] getImageBytes() {
        if (image == null) {
            return null;
        }

        try {
            return java.nio.file.Files.readAllBytes(image.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
