package service;

import dao.OrderDAO;
import model.OrderRecord;
import model.Product;
import model.PurchaseResult;

/**
 * Business service for order workflows.
 */
public class OrderService {
    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductService productService = new ProductService();

    public PurchaseResult purchaseProduct(int productId, int buyerId) {
        Product product = productService.getProductDetail(productId);
        if (product == null) {
            return PurchaseResult.NOT_FOUND;
        }
        if (!"ACTIVE".equalsIgnoreCase(product.getStatus())) {
            return PurchaseResult.ALREADY_SOLD;
        }
        if (product.getSellerId() == buyerId) {
            return PurchaseResult.OWN_PRODUCT;
        }

        boolean sold = productService.markAsSold(productId);
        if (!sold) {
            return PurchaseResult.ALREADY_SOLD;
        }

        boolean orderCreated = orderDAO.createOrder(new OrderRecord(0, productId, buyerId, product.getSellerId(), product.getPrice()));
        return orderCreated ? PurchaseResult.SUCCESS : PurchaseResult.DB_ERROR;
    }
}
