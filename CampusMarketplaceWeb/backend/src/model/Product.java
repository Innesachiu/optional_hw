package model;

import java.sql.Timestamp;

/**
 * Product entity mapping products table.
 */
public class Product {
    private int productId;
    private int sellerId;
    private Integer categoryId;
    private String title;
    private int price;
    private String description;
    private String status;
    private int searchHitCount;
    private Timestamp createdAt;
    private String imageUrl;

    /** Default constructor. */
    public Product() {}
    /** @return product id */
    public int getProductId() { return productId; }
    /** @param productId product id */
    public void setProductId(int productId) { this.productId = productId; }
    /** @return seller id */
    public int getSellerId() { return sellerId; }
    /** @param sellerId seller id */
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }
    /** @return category id */
    public Integer getCategoryId() { return categoryId; }
    /** @param categoryId category id */
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    /** @return title */
    public String getTitle() { return title; }
    /** @param title title */
    public void setTitle(String title) { this.title = title; }
    /** @return price */
    public int getPrice() { return price; }
    /** @param price price */
    public void setPrice(int price) { this.price = price; }
    /** @return description */
    public String getDescription() { return description; }
    /** @param description description */
    public void setDescription(String description) { this.description = description; }
    /** @return status */
    public String getStatus() { return status; }
    /** @param status status */
    public void setStatus(String status) { this.status = status; }
    /** @return search hit count */
    public int getSearchHitCount() { return searchHitCount; }
    /** @param searchHitCount search hit count */
    public void setSearchHitCount(int searchHitCount) { this.searchHitCount = searchHitCount; }
    /** @return created time */
    public Timestamp getCreatedAt() { return createdAt; }
    /** @param createdAt created time */
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    /** @return image url */
    public String getImageUrl() { return imageUrl; }
    /** @param imageUrl image url */
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
