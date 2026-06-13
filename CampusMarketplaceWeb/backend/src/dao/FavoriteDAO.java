package dao;

import model.Favorite;
import model.Product;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access for favorites table.
 */
public class FavoriteDAO {
    /**
     * Adds favorite; returns true if favorite exists after call (inserted or duplicate).
     */
    public boolean addFavorite(int userId, int productId) {
        String sql = "INSERT INTO favorites (user_id, product_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            // handle duplicate key gracefully
            String state = e.getSQLState();
            if (state != null && state.startsWith("23")) {
                // integrity constraint violation - already exists
                return true;
            }
            return false;
        }
    }

    /**
     * Removes a favorite.
     */
    public boolean removeFavorite(int userId, int productId) {
        String sql = "DELETE FROM favorites WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Checks whether favorite exists.
     */
    public boolean exists(int userId, int productId) {
        String sql = "SELECT 1 FROM favorites WHERE user_id = ? AND product_id = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Finds products favorited by a user (most recent first).
     */
    public List<Product> findProductsByUserId(int userId) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.product_id,p.seller_id,p.category_id,p.title,p.price,p.description,p.status,p.search_hit_count,p.created_at " +
                "FROM favorites f JOIN products p ON p.product_id = f.product_id WHERE f.user_id = ? ORDER BY f.created_at DESC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setProductId(rs.getInt("product_id"));
                    p.setSellerId(rs.getInt("seller_id"));
                    int cat = rs.getInt("category_id");
                    p.setCategoryId(rs.wasNull() ? null : cat);
                    p.setTitle(rs.getString("title"));
                    p.setPrice(rs.getInt("price"));
                    p.setDescription(rs.getString("description"));
                    p.setStatus(rs.getString("status"));
                    p.setSearchHitCount(rs.getInt("search_hit_count"));
                    p.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            return list;
        }
        return list;
    }
}
