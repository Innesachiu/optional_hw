package controller;

import model.PurchaseResult;
import service.OrderService;

/**
 * Controller for order actions.
 */
public class OrderController {
    private final OrderService orderService = new OrderService();

    public PurchaseResult createOrder(int productId, int buyerId) {
        return orderService.purchaseProduct(productId, buyerId);
    }
}
