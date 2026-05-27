package dto;

/**
 * Request payload for order API.
 */
public class OrderRequest {
    private int buyerId;
    private int productId;

    /** Default constructor. */
    public OrderRequest() {}
    /** @return buyer id */
    public int getBuyerId() { return buyerId; }
    /** @param buyerId buyer id */
    public void setBuyerId(int buyerId) { this.buyerId = buyerId; }
    /** @return product id */
    public int getProductId() { return productId; }
    /** @param productId product id */
    public void setProductId(int productId) { this.productId = productId; }
}
