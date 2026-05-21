package model;

/**
 * Represents a purchase order.
 */
public class OrderRecord {
    private int id;
    private int productId;
    private int buyerId;
    private int sellerId;
    private double orderPrice;

    public OrderRecord() {}

    public OrderRecord(int id, int productId, int buyerId, int sellerId, double orderPrice) {
        this.id = id;
        this.productId = productId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.orderPrice = orderPrice;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getBuyerId() { return buyerId; }
    public void setBuyerId(int buyerId) { this.buyerId = buyerId; }
    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }
    public double getOrderPrice() { return orderPrice; }
    public void setOrderPrice(double orderPrice) { this.orderPrice = orderPrice; }
}
