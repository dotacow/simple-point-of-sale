package controllers;

import models.User;
import utils.DBHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    public static void addUser(User user) {
        String sql = "INSERT INTO user (Name, Role, Password, Username) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getRole().name());
            stmt.setString(3, user.getPwd());
            stmt.setString(4, user.getUsrName());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(int id) {
        String sql = "DELETE FROM user WHERE userId = ?";
        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editUser(User user) {
        String sql = "UPDATE user SET name = ?, role = ?, password = ?, username = ? WHERE userId = ?";
        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getRole().name());
            stmt.setString(3, user.getPwd());
            stmt.setString(4, user.getUsrName());
            stmt.setInt(5, user.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User getUserById(int id) {
        String sql = "SELECT * FROM user WHERE userId = ?";
        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("UserId"),
                    rs.getString("name"),
                    User.Role.valueOf(rs.getString("role").toUpperCase()),
                    rs.getString("password"),
                    rs.getString("username")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";

        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                    rs.getInt("UserId"),
                    rs.getString("Name"),
                    User.Role.valueOf(rs.getString("Role").toUpperCase()),
                    rs.getString("Password"),
                    rs.getString("Username")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    public static User authenticate(String username, String password) {
    String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
    try (Connection conn = DBHelper.connect();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
         System.out.println("[" + username + "] [" + password + "]");
        if (rs.next()) {
            return new User(
                rs.getInt("UserId"),
                rs.getString("Name"),
                User.Role.valueOf(rs.getString("Role").toUpperCase()),
                rs.getString("Password"),
                rs.getString("Username")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

}
