package controller;

import dto.ApiResponse;
import dto.OrderRequest;
import exception.AppException;
import service.OrderService;

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
}
