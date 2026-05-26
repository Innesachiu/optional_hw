package dao;

import model.Product;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access for products table.
 */
public class ProductDAO {
    /**
     * Inserts a product.
     *
     * @param product product data
     * @return true if inserted
     */
    public boolean create(Product product) {
        String sql = "INSERT INTO products(seller_id,category_id,title,price,description,status) VALUES(?,?,?,?,?,'ACTIVE')";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, product.getSellerId());
            if (product.getCategoryId() == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, product.getCategoryId());
            }
            ps.setString(3, product.getTitle());
            ps.setInt(4, product.getPrice());
            ps.setString(5, product.getDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Gets ACTIVE products.
     *
     * @return product list
     */
    public List<Product> findActive() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT product_id,seller_id,category_id,title,price,description,status,search_hit_count,created_at FROM products WHERE status='ACTIVE' ORDER BY product_id DESC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            return list;
        }
        return list;
    }

    /**
     * Searches active products by keyword.
     *
     * @param keyword keyword
     * @return product list
     */
    public List<Product> searchActive(String keyword) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT product_id,seller_id,category_id,title,price,description,status,search_hit_count,created_at FROM products WHERE status='ACTIVE' AND (title LIKE ? OR description LIKE ?) ORDER BY product_id DESC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            return list;
        }
        return list;
    }

    /**
     * Marks product sold if active.
     *
     * @param productId product id
     * @return true if updated
     */
    public boolean markSold(int productId) {
        String sql = "UPDATE products SET status='SOLD' WHERE product_id=? AND status='ACTIVE'";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    private Product map(ResultSet rs) throws SQLException {
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
        return p;
    }
}
