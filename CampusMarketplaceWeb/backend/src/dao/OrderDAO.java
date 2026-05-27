package dao;

import model.Order;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Data access for orders table.
 */
public class OrderDAO {
    /**
     * Inserts an order.
     *
     * @param order order data
     * @return true if inserted
     */
    public boolean create(Order order) {
        String sql = "INSERT INTO orders(buyer_id,seller_id,product_id,amount,status) VALUES(?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, order.getBuyerId());
            ps.setInt(2, order.getSellerId());
            ps.setInt(3, order.getProductId());
            ps.setInt(4, order.getAmount());
            ps.setString(5, order.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
