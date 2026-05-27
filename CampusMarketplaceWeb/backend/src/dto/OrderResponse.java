package dto;

/**
 * Response payload for order list API.
 */
public class OrderResponse {
    private int orderId;
    private int productId;
    private String productTitle;
    private int price;
    private int sellerId;
    private int buyerId;
    private String status;
    private String createdAt;

    /** Default constructor. */
    public OrderResponse() {}
    /** @return order id */
    public int getOrderId() { return orderId; }
    /** @param orderId order id */
    public void setOrderId(int orderId) { this.orderId = orderId; }
    /** @return product id */
    public int getProductId() { return productId; }
    /** @param productId product id */
    public void setProductId(int productId) { this.productId = productId; }
    /** @return product title */
    public String getProductTitle() { return productTitle; }
    /** @param productTitle title */
    public void setProductTitle(String productTitle) { this.productTitle = productTitle; }
    /** @return price */
    public int getPrice() { return price; }
    /** @param price price */
    public void setPrice(int price) { this.price = price; }
    /** @return seller id */
    public int getSellerId() { return sellerId; }
    /** @param sellerId seller id */
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }
    /** @return buyer id */
    public int getBuyerId() { return buyerId; }
    /** @param buyerId buyer id */
    public void setBuyerId(int buyerId) { this.buyerId = buyerId; }
    /** @return status */
    public String getStatus() { return status; }
    /** @param status status */
    public void setStatus(String status) { this.status = status; }
    /** @return created at */
    public String getCreatedAt() { return createdAt; }
    /** @param createdAt created at */
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
