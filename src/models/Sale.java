package models;

import java.sql.Timestamp;
import java.sql.*;
import utils.DBHelper;
import models.User.Role;

public class Sale {

    public enum PaymentMethod {
        CASH, CREDIT
    }

    private int id;
    private int userId;
    private Timestamp createdAtTime;
    private float totalPrice;
    private PaymentMethod paymentMethod;

    // Constructors
    public Sale(int id, int userId, Timestamp createdAtTime, float totalPrice, PaymentMethod paymentMethod) {
        this.id = id;
        this.userId = userId;
        this.createdAtTime = createdAtTime;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
    }

    public Sale(int usrId, float totalPrice, PaymentMethod paymentMethod) {
        this.userId = usrId;
        this.createdAtTime = new Timestamp(System.currentTimeMillis());
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
    }

    // Getters and setters...
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int usrId) {
        this.userId = usrId;
    }

    public User getUser() {
        User user = null;
        try (Connection conn = DBHelper.connect(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE userId = ?")) {

            stmt.setInt(1, this.userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("userId");
                    String name = rs.getString("name");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String roleStr = rs.getString("role");
                    Role role = Role.valueOf(roleStr.toUpperCase());

                    user = new User(id, name, role, password, username);
                }
            }

        } catch (SQLException e) {
            System.err.println("User with ID: " + this.userId + " is not found.");
            e.printStackTrace();
        }

        return user;
    }

    public Timestamp getCreatedAtTime() {
        return createdAtTime;
    }

    public void setCreatedAtTime(Timestamp createdAtTime) {
        this.createdAtTime = createdAtTime;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
