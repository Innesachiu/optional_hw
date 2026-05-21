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
 * DAO for product operations.
 */
public class ProductDAO {
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products(seller_id, title, description, price, status) VALUES(?, ?, ?, ?, 'ACTIVE')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, product.getSellerId());
            ps.setString(2, product.getTitle());
            ps.setString(3, product.getDescription());
            ps.setDouble(4, product.getPrice());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Product> getActiveProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, seller_id, title, description, price, status FROM products WHERE status = 'ACTIVE' ORDER BY id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                products.add(toProduct(rs));
            }
        } catch (SQLException e) {
            return products;
        }
        return products;
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, seller_id, title, description, price, status FROM products WHERE status = 'ACTIVE' AND (title LIKE ? OR description LIKE ?) ORDER BY id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(toProduct(rs));
                }
            }
        } catch (SQLException e) {
            return products;
        }
        return products;
    }

    public Product getProductById(int id) {
        String sql = "SELECT id, seller_id, title, description, price, status FROM products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return toProduct(rs);
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    public boolean markAsSold(int productId) {
        String sql = "UPDATE products SET status = 'SOLD' WHERE id = ? AND status = 'ACTIVE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    private Product toProduct(ResultSet rs) throws SQLException {
        return new Product(rs.getInt("id"), rs.getInt("seller_id"), rs.getString("title"),
                rs.getString("description"), rs.getDouble("price"), rs.getString("status"));
    }
}
