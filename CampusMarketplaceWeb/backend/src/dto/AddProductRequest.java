package dto;

/**
 * Request payload for add product API.
 */
public class AddProductRequest {
    private int sellerId;
    private Integer categoryId;
    private String title;
    private int price;
    private String description;

    /** Default constructor. */
    public AddProductRequest() {}
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
}
