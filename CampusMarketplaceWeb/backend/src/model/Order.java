package model;

import java.sql.Timestamp;

/**
 * Order entity mapping orders table.
 */
public class Order {
    private int orderId;
    private int buyerId;
    private int sellerId;
    private int productId;
    private int amount;
    private String status;
    private Timestamp createdAt;

    /** Default constructor. */
    public Order() {}
    /** @return order id */
    public int getOrderId() { return orderId; }
    /** @param orderId order id */
    public void setOrderId(int orderId) { this.orderId = orderId; }
    /** @return buyer id */
    public int getBuyerId() { return buyerId; }
    /** @param buyerId buyer id */
    public void setBuyerId(int buyerId) { this.buyerId = buyerId; }
    /** @return seller id */
    public int getSellerId() { return sellerId; }
    /** @param sellerId seller id */
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }
    /** @return product id */
    public int getProductId() { return productId; }
    /** @param productId product id */
    public void setProductId(int productId) { this.productId = productId; }
    /** @return amount */
    public int getAmount() { return amount; }
    /** @param amount amount */
    public void setAmount(int amount) { this.amount = amount; }
    /** @return status */
    public String getStatus() { return status; }
    /** @param status status */
    public void setStatus(String status) { this.status = status; }
    /** @return created time */
    public Timestamp getCreatedAt() { return createdAt; }
    /** @param createdAt created time */
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
