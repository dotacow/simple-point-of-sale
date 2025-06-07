package utils;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;

/**
 *
 * @author yousef
 */
public class DBHelper {

    private static final String URL = "jdbc:mysql://localhost:3306/pos";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Failed to connect to database.");
            e.printStackTrace();
            return null;
        }
    }

    public static File writeBlobToTempFile(Blob blob) {
    try {
        byte[] bytes = blob.getBytes(1, (int) blob.length());
        File temp = File.createTempFile("img_", ".tmp");
        try (FileOutputStream fos = new FileOutputStream(temp)) {
            fos.write(bytes);
        }
        return temp;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
}

