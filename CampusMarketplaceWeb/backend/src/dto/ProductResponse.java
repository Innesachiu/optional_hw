package dto;

/**
 * Product summary response for list/search APIs.
 */
public class ProductResponse {
    private int productId;
    private String title;
    private int price;
    private String status;
    private String imageUrl;

    /** Default constructor. */
    public ProductResponse() {}
    /** @return product id */
    public int getProductId() { return productId; }
    /** @param productId product id */
    public void setProductId(int productId) { this.productId = productId; }
    /** @return title */
    public String getTitle() { return title; }
    /** @param title title */
    public void setTitle(String title) { this.title = title; }
    /** @return price */
    public int getPrice() { return price; }
    /** @param price price */
    public void setPrice(int price) { this.price = price; }
    /** @return status */
    public String getStatus() { return status; }
    /** @param status status */
    public void setStatus(String status) { this.status = status; }
    /** @return image url */
    public String getImageUrl() { return imageUrl; }
    /** @param imageUrl image url */
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
