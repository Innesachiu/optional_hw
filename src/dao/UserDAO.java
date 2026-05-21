package dao;

import model.User;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO for user operations.
 */
public class UserDAO {
    /**
     * Registers a new user.
     *
     * @param user user data
     * @return true if insert success
     */
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

    /**
     * Finds user by username and password hash.
     *
     * @param username username
     * @param passwordHash password hash
     * @return matched user or null
     */
    public User login(String username, String passwordHash) {
        String sql = "SELECT id, username, password_hash FROM users WHERE username = ? AND password_hash = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
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
