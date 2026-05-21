package dao;

import model.User;
import util.DBConnection;

import java.sql.*;

/**
 * DAO for user operations.
 */
public class UserDAO {
    public boolean register(User user) {
        String sql = "INSERT INTO users(username, password_hash) VALUES(?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public User login(String username, String password) {
        String sql = "SELECT id, username, password_hash FROM users WHERE username = ? AND password_hash = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password_hash"));
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }
}
