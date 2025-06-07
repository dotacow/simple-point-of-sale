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
        String sql = "SELECT id, name, role, pwd, usrName FROM users";

        try (Connection conn = DBHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Role role = Role.valueOf(rs.getString("role"));
                String pwd = rs.getString("pwd");
                String usrName = rs.getString("usrName");

                User user = new User(id, name, role, pwd, usrName);
                users.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle or throw
        }
        return users;
    }

    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (id, name, role, pwd, usrName) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBHelper.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getRole().name());
            ps.setString(4, user.getPwd());
            ps.setString(5, user.getUsrName());

            ps.executeUpdate();
        }
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, role = ?, pwd = ?, usrName = ? WHERE id = ?";

        try (Connection conn = DBHelper.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getRole().name());
            ps.setString(3, user.getPwd());
            ps.setString(4, user.getUsrName());
            ps.setInt(5, user.getId());

            ps.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DBHelper.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
}
