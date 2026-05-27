package dao;

import dto.OrderResponse;
import model.Order;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Finds orders by buyer id.
     *
     * @param buyerId buyer id
     * @return order response list
     */
    public List<OrderResponse> findOrdersByBuyerId(int buyerId) {
        List<OrderResponse> list = new ArrayList<>();
        String sql = "SELECT o.order_id,o.product_id,p.title,o.amount,o.seller_id,o.buyer_id,o.status,o.created_at " +
                "FROM orders o JOIN products p ON o.product_id=p.product_id WHERE o.buyer_id=? ORDER BY o.order_id DESC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, buyerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderResponse r = new OrderResponse();
                    r.setOrderId(rs.getInt("order_id"));
                    r.setProductId(rs.getInt("product_id"));
                    r.setProductTitle(rs.getString("title"));
                    r.setPrice(rs.getInt("amount"));
                    r.setSellerId(rs.getInt("seller_id"));
                    r.setBuyerId(rs.getInt("buyer_id"));
                    r.setStatus(rs.getString("status"));
                    r.setCreatedAt(String.valueOf(rs.getTimestamp("created_at")));
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            return list;
        }
        return list;
    }
}
