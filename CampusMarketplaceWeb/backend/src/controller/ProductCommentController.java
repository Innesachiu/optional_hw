package controller;

import dto.ApiResponse;
import dto.ProductCommentResponse;
import service.ProductCommentService;

import java.util.List;

/**
 * Controller for product comment APIs.
 */
public class ProductCommentController {
    private final ProductCommentService productCommentService = new ProductCommentService();

    /**
     * Handles GET /api/products/{productId}/comments.
     *
     * @param productId product id
     * @return comment list
     */
    public List<ProductCommentResponse> listComments(int productId) {
        return productCommentService.listComments(productId);
    }

    /**
     * Handles POST /api/products/{productId}/comments.
     *
     * @param productId product id
     * @param userId user id
     * @param content comment content
     * @return API response containing created comment
     */
    public ApiResponse addComment(int productId, int userId, String content) {
        ProductCommentResponse comment = productCommentService.addComment(productId, userId, content);
        ApiResponse response = new ApiResponse(true, "留言已送出。");
        response.setData(toCommentJson(comment));
        return response;
    }

    /**
     * Handles DELETE /api/comments/{commentId}.
     *
     * @param commentId comment id
     * @param userId user id
     * @return API response
     */
    public ApiResponse deleteComment(int commentId, int userId) {
        productCommentService.deleteComment(commentId, userId);
        return new ApiResponse(true, "留言已刪除。");
    }

    /**
     * Converts comment response to JSON.
     *
     * @param comment comment response
     * @return JSON object
     */
    public String toCommentJson(ProductCommentResponse comment) {
        return "{\"commentId\":" + comment.getCommentId()
                + ",\"productId\":" + comment.getProductId()
                + ",\"userId\":" + comment.getUserId()
                + ",\"username\":\"" + util.JsonUtil.escape(comment.getUsername()) + "\""
                + ",\"content\":\"" + util.JsonUtil.escape(comment.getContent()) + "\""
                + ",\"createdAt\":\"" + util.JsonUtil.escape(comment.getCreatedAt()) + "\"}";
    }
}
