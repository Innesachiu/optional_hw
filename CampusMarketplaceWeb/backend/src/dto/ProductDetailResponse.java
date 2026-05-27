package dto;

/**
 * Product detail response object.
 */
public class ProductDetailResponse extends ProductResponse {
    private int sellerId;
    private Integer categoryId;
    private String description;
    private int searchHitCount;

    /** Default constructor. */
    public ProductDetailResponse() {}
    /** @return seller id */
    public int getSellerId() { return sellerId; }
    /** @param sellerId seller id */
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }
    /** @return category id */
    public Integer getCategoryId() { return categoryId; }
    /** @param categoryId category id */
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    /** @return description */
    public String getDescription() { return description; }
    /** @param description description */
    public void setDescription(String description) { this.description = description; }
    /** @return search hit count */
    public int getSearchHitCount() { return searchHitCount; }
    /** @param searchHitCount search hit count */
    public void setSearchHitCount(int searchHitCount) { this.searchHitCount = searchHitCount; }
}
