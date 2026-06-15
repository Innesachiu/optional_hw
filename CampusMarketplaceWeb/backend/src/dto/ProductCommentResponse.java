package dto;

/**
 * Response DTO for product comments.
 */
public class ProductCommentResponse {
    private int commentId;
    private int productId;
    private int userId;
    private String username;
    private String content;
    private String createdAt;

    /** Default constructor. */
    public ProductCommentResponse() {}

    /** @return comment id */
    public int getCommentId() { return commentId; }
    /** @param commentId comment id */
    public void setCommentId(int commentId) { this.commentId = commentId; }
    /** @return product id */
    public int getProductId() { return productId; }
    /** @param productId product id */
    public void setProductId(int productId) { this.productId = productId; }
    /** @return user id */
    public int getUserId() { return userId; }
    /** @param userId user id */
    public void setUserId(int userId) { this.userId = userId; }
    /** @return username */
    public String getUsername() { return username; }
    /** @param username username */
    public void setUsername(String username) { this.username = username; }
    /** @return comment content */
    public String getContent() { return content; }
    /** @param content comment content */
    public void setContent(String content) { this.content = content; }
    /** @return created time text */
    public String getCreatedAt() { return createdAt; }
    /** @param createdAt created time text */
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
