package service;

import dao.ProductCommentDAO;
import dao.ProductDAO;
import dao.UserDAO;
import dto.ProductCommentResponse;
import exception.AuthException;
import exception.DatabaseException;
import exception.NotFoundException;
import exception.ValidationException;
import model.Product;
import model.ProductComment;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles product comment business logic.
 */
public class ProductCommentService {
    private final ProductCommentDAO productCommentDAO = new ProductCommentDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final UserDAO userDAO = new UserDAO();

    /**
     * Lists comments for one product.
     *
     * @param productId product id
     * @return comment response list
     */
    public List<ProductCommentResponse> listComments(int productId) {
        if (productId <= 0) {
            throw new ValidationException("invalid productId");
        }
        Product product = productDAO.findById(productId);
        if (product == null) {
            throw new NotFoundException("product not found");
        }
        return toResponseList(productCommentDAO.findByProductId(productId));
    }

    /**
     * Creates a product comment.
     *
     * @param productId product id
     * @param userId user id
     * @param content raw comment content
     * @return created comment response
     */
    public ProductCommentResponse addComment(int productId, int userId, String content) {
        if (productId <= 0) {
            throw new ValidationException("invalid productId");
        }
        if (userId <= 0) {
            throw new ValidationException("invalid userId");
        }
        Product product = productDAO.findById(productId);
        if (product == null) {
            throw new NotFoundException("product not found");
        }
        String status = product.getStatus() == null ? "" : product.getStatus();
        if ("REMOVED".equals(status) || "DELETED".equals(status)) {
            throw new ValidationException("商品已下架，無法留言。");
        }
        if (userDAO.findById(userId) == null) {
            throw new NotFoundException("user not found");
        }
        String trimmed = content == null ? "" : content.trim();
        if (trimmed.isEmpty()) {
            throw new ValidationException("留言內容不可為空。");
        }
        if (trimmed.length() > 500) {
            throw new ValidationException("留言內容不可超過 500 字。");
        }
        int commentId = productCommentDAO.create(productId, userId, trimmed);
        if (commentId <= 0) {
            throw new DatabaseException("failed to add comment");
        }
        ProductComment created = productCommentDAO.findById(commentId);
        if (created == null) {
            throw new DatabaseException("failed to load comment");
        }
        return toResponse(created);
    }

    /**
     * Deletes a comment after checking owner.
     *
     * @param commentId comment id
     * @param userId request user id
     */
    public void deleteComment(int commentId, int userId) {
        if (commentId <= 0) {
            throw new ValidationException("invalid commentId");
        }
        if (userId <= 0) {
            throw new ValidationException("invalid userId");
        }
        ProductComment comment = productCommentDAO.findById(commentId);
        if (comment == null) {
            throw new NotFoundException("comment not found");
        }
        if (comment.getUserId() != userId) {
            throw new AuthException("只能刪除自己的留言。");
        }
        if (!productCommentDAO.deleteById(commentId)) {
            throw new DatabaseException("failed to delete comment");
        }
    }

    private List<ProductCommentResponse> toResponseList(List<ProductComment> comments) {
        List<ProductCommentResponse> list = new ArrayList<>();
        for (ProductComment comment : comments) {
            list.add(toResponse(comment));
        }
        return list;
    }

    private ProductCommentResponse toResponse(ProductComment comment) {
        ProductCommentResponse response = new ProductCommentResponse();
        response.setCommentId(comment.getCommentId());
        response.setProductId(comment.getProductId());
        response.setUserId(comment.getUserId());
        response.setUsername(comment.getUsername());
        response.setContent(comment.getContent());
        response.setCreatedAt(formatTimestamp(comment.getCreatedAt()));
        return response;
    }

    private String formatTimestamp(Timestamp timestamp) {
        return timestamp == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
    }
}
