package dao;

import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO for product_images table. Keeps APIs minimal: find primary image, insert and delete by product.
 */
public class ProductImageDAO {
    public ProductImageDAO() {}

    /**
     * Returns primary image url for product or null.
     */
    public String findPrimaryImageUrl(int productId) {
        String sql = "SELECT image_url FROM product_images WHERE product_id=? ORDER BY sort_order ASC LIMIT 1";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("image_url");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deletes all images for the given product.
     */
    public boolean deleteByProductId(int productId) {
        String sql = "DELETE FROM product_images WHERE product_id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inserts a new image record for a product. Returns true on success.
     */
    public boolean insert(int productId, String imageUrl) {
        String sql = "INSERT INTO product_images(product_id,image_url,sort_order) VALUES(?,?,0)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setString(2, imageUrl);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
