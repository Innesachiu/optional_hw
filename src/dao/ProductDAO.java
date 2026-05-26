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
 * DAO for products.
 */
public class ProductDAO {
    public List<Product> findActiveProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.product_id,p.seller_id,p.category_id,p.title,p.price,p.description,p.status,p.search_hit_count,u.username,c.name " +
                "FROM products p JOIN users u ON p.seller_id=u.user_id " +
                "LEFT JOIN categories c ON p.category_id=c.category_id WHERE p.status='ACTIVE' ORDER BY p.product_id DESC";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql); ResultSet r = p.executeQuery()) {
            while (r.next()) list.add(map(r));
        } catch (SQLException ignored) {
        }
        return list;
    }

    public List<Product> searchActiveProducts(String keyword) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.product_id,p.seller_id,p.category_id,p.title,p.price,p.description,p.status,p.search_hit_count,u.username,c.name " +
                "FROM products p JOIN users u ON p.seller_id=u.user_id " +
                "LEFT JOIN categories c ON p.category_id=c.category_id " +
                "WHERE p.status='ACTIVE' AND (p.title LIKE ? OR p.description LIKE ?) ORDER BY p.product_id DESC";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            String k = "%" + keyword + "%";
            p.setString(1, k);
            p.setString(2, k);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) list.add(map(r));
            }
        } catch (SQLException ignored) {
        }
        return list;
    }

    public boolean insertProduct(Product product) {
        String sql = "INSERT INTO products(seller_id,category_id,title,price,description,status) VALUES(?,?,?,?,?,'ACTIVE')";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, product.getSellerId());
            p.setInt(2, product.getCategoryId());
            p.setString(3, product.getTitle());
            p.setInt(4, product.getPrice());
            p.setString(5, product.getDescription());
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public Product findById(int productId) {
        String sql = "SELECT p.product_id,p.seller_id,p.category_id,p.title,p.price,p.description,p.status,p.search_hit_count,u.username,c.name " +
                "FROM products p JOIN users u ON p.seller_id=u.user_id " +
                "LEFT JOIN categories c ON p.category_id=c.category_id WHERE p.product_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, productId);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) return map(r);
            }
        } catch (SQLException ignored) {
        }
        return null;
    }

    public boolean increaseSearchHit(int productId) {
        String sql = "UPDATE products SET search_hit_count=search_hit_count+1 WHERE product_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, productId);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean markSold(int productId) {
        try (Connection c = DBConnection.getConnection()) {
            return markSold(c, productId);
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean markSold(Connection connection, int productId) throws SQLException {
        String sql = "UPDATE products SET status='SOLD' WHERE product_id=? AND status='ACTIVE'";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId);
            return statement.executeUpdate() > 0;
        }
    }

    private Product map(ResultSet r) throws SQLException {
        Product p = new Product();
        p.setProductId(r.getInt(1));
        p.setSellerId(r.getInt(2));
        p.setCategoryId(r.getInt(3));
        p.setTitle(r.getString(4));
        p.setPrice(r.getInt(5));
        p.setDescription(r.getString(6));
        p.setStatus(r.getString(7));
        p.setSearchHitCount(r.getInt(8));
        p.setSellerName(r.getString(9));
        p.setCategoryName(r.getString(10));
        return p;
    }
}
