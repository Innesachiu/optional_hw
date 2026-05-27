package service;

import dao.OrderDAO;
import dao.ProductDAO;
import dto.OrderRequest;
import dto.OrderResponse;
import exception.DatabaseException;
import exception.NotFoundException;
import exception.ValidationException;
import model.Order;
import model.Product;

import java.util.List;

/**
 * Handles order business logic.
 */
public class OrderService {
    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductDAO productDAO = new ProductDAO();

    /**
     * Gets orders of a buyer.
     *
     * @param buyerId buyer id
     * @return order list
     */
    public List<OrderResponse> getOrdersByBuyerId(int buyerId) {
        if (buyerId <= 0) {
            throw new ValidationException("invalid buyer id");
        }
        return orderDAO.findOrdersByBuyerId(buyerId);
    }

    /**
     * Creates an order and marks the target product as sold.
     *
     * @param request order request
     */
    public void createOrder(OrderRequest request) {
        if (request == null || request.getBuyerId() <= 0 || request.getProductId() <= 0) {
            throw new ValidationException("invalid order request");
        }
        Product product = productDAO.findById(request.getProductId());
        if (product == null) {
            throw new NotFoundException("product not found");
        }
        if (!"ACTIVE".equals(product.getStatus())) {
            throw new ValidationException("product is not active");
        }
        if (product.getSellerId() == request.getBuyerId()) {
            throw new ValidationException("cannot buy your own product");
        }

        Order order = new Order();
        order.setBuyerId(request.getBuyerId());
        order.setSellerId(product.getSellerId());
        order.setProductId(product.getProductId());
        order.setAmount(product.getPrice());
        order.setStatus("COMPLETED");

        if (!orderDAO.create(order)) {
            throw new DatabaseException("failed to create order");
        }
        if (!productDAO.markSold(product.getProductId())) {
            throw new DatabaseException("failed to update product status");
        }
    }
}
