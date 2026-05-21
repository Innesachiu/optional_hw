package service;

import dao.OrderDAO;
import dao.ProductDAO;
import model.Order;
import model.Product;
import util.DBConnection;

import java.sql.Connection;

/**
 * Service class for order creation and purchase rules.
 */
public class OrderService {
    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductDAO productDAO = new ProductDAO();

    /**
     * Places an order for a product.
     * Rules:
     * 1) Product must exist and be ACTIVE
     * 2) Buyer cannot buy own product
     * 3) Create order and mark product SOLD in one transaction
     *
     * @param buyerId   buyer user id
     * @param productId product id
     * @return result message for UI
     */
    public String placeOrder(int buyerId, int productId) {
        Product product = productDAO.findById(productId);
        if (product == null) {
            return "商品不存在";
        }
        if (!"ACTIVE".equals(product.getStatus())) {
            return "商品已售出";
        }
        if (product.getSellerId() == buyerId) {
            return "不能購買自己的商品";
        }

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            Order order = new Order();
            order.setBuyerId(buyerId);
            order.setSellerId(product.getSellerId());
            order.setProductId(product.getProductId());
            order.setAmount(product.getPrice());
            order.setStatus("COMPLETED");

            boolean orderCreated = orderDAO.createOrder(connection, order);
            boolean sold = productDAO.markSold(connection, productId);

            if (orderCreated && sold) {
                connection.commit();
                return "下訂單成功";
            }

            connection.rollback();
            return "下訂單失敗，請稍後再試";
        } catch (Exception ex) {
            return "系統錯誤：" + ex.getMessage();
        }
    }
}
