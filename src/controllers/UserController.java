package controllers;

import models.User;
import models.User.Role;
import utils.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT userId, name, role, password, username FROM user";

        try (Connection conn = DBHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }

        } catch (SQLException ex) {
            System.err.println("Error fetching users: " + ex.getMessage());
            ex.printStackTrace();
        }

        return users;
    }


    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO user (userId, name, role, password, username) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBHelper.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getRole().name());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getUsername());

            ps.executeUpdate();
        }
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE user SET name = ?, role = ?, password = ?, username = ? WHERE userId = ?";

        try (Connection conn = DBHelper.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getRole().name());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getUsername());
            ps.setInt(5, user.getId());

            ps.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM user WHERE userId = ?";

        try (Connection conn = DBHelper.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    public static User authenticate(String username, String password) {
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";

        try (Connection conn = DBHelper.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Authentication failed: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private static User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("userId"),
                rs.getString("name"),
                Role.valueOf(rs.getString("role").toUpperCase()),
                rs.getString("password"),
                rs.getString("username")
        );
    }
}
