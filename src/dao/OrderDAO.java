package dao;

import model.OrderRecord;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DAO for order operations.
 */
public class OrderDAO {
    public boolean createOrder(OrderRecord order) {
        String sql = "INSERT INTO orders(product_id, buyer_id, seller_id, order_price) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, order.getProductId());
            ps.setInt(2, order.getBuyerId());
            ps.setInt(3, order.getSellerId());
            ps.setDouble(4, order.getOrderPrice());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
