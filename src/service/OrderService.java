package service;

import dao.OrderDAO;
import model.OrderRecord;

/**
 * Business service for order workflows.
 */
public class OrderService {
    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductService productService = new ProductService();

    public boolean purchaseProduct(int productId, int buyerId, int sellerId, double price) {
        boolean sold = productService.markAsSold(productId);
        if (!sold) {
            return false;
        }
        return orderDAO.createOrder(new OrderRecord(0, productId, buyerId, sellerId, price));
    }
}
