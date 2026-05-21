package dao;
import model.Order;import util.DBConnection;import java.sql.*;
/** DAO for orders. */
public class OrderDAO {
    /** @param order order @return success */
    public boolean createOrder(Order order){String sql="INSERT INTO orders(buyer_id,seller_id,product_id,amount,status) VALUES(?,?,?,?,?)";try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setInt(1,order.getBuyerId());p.setInt(2,order.getSellerId());p.setInt(3,order.getProductId());p.setInt(4,order.getAmount());p.setString(5,order.getStatus());return p.executeUpdate()>0;}catch(SQLException e){return false;}}
}
