package controller;

import service.OrderService;

/**
 * Controller for order actions.
 */
public class OrderController {
    private final OrderService orderService = new OrderService();

    public boolean createOrder(int productId, int buyerId, int sellerId, double price) {
        return orderService.purchaseProduct(productId, buyerId, sellerId, price);
    }
}
