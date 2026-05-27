package dao;

import model.Order;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DAO for order operations.
 */
public class OrderDAO {
    /**
     * Creates an order with an internal connection.
     *
     * @param order order data
     * @return true if inserted
     */
    public boolean createOrder(Order order) {
        try (Connection connection = DBConnection.getConnection()) {
            return createOrder(connection, order);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Creates an order using provided transaction connection.
     *
     * @param connection jdbc connection
     * @param order      order data
     * @return true if inserted
     * @throws SQLException db error
     */
    public boolean createOrder(Connection connection, Order order) throws SQLException {
        String sql = "INSERT INTO orders(buyer_id,seller_id,product_id,amount,status) VALUES(?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, order.getBuyerId());
            statement.setInt(2, order.getSellerId());
            statement.setInt(3, order.getProductId());
            statement.setInt(4, order.getAmount());
            statement.setString(5, order.getStatus());
            return statement.executeUpdate() > 0;
        }
    }
}
