package controller;

import dto.ApiResponse;
import dto.OrderRequest;
import dto.OrderResponse;
import exception.AppException;
import service.OrderService;

import java.util.List;

/**
 * Controller for order APIs.
 */
public class OrderController {
    private final OrderService orderService = new OrderService();

    /**
     * Handles POST /api/orders.
     *
     * @param request order request
     * @return API response
     */
    public ApiResponse createOrder(OrderRequest request) {
        try {
            orderService.createOrder(request);
            return new ApiResponse(true, "order created");
        } catch (AppException e) {
            return new ApiResponse(false, e.getMessage());
        }
    }

    /**
     * Handles GET /api/orders/my?buyerId=xxx.
     *
     * @param buyerId buyer id
     * @return order response list
     */
    public List<OrderResponse> myOrders(int buyerId) {
        return orderService.getOrdersByBuyerId(buyerId);
    }
}
