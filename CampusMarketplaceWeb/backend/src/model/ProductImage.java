package model;

/**
 * Product image entity mapping product_images table.
 */
public class ProductImage {
    private int imageId;
    private int productId;
    private String imageUrl;
    private int sortOrder;

    /** Default constructor. */
    public ProductImage() {}
    /** @return image id */
    public int getImageId() { return imageId; }
    /** @param imageId image id */
    public void setImageId(int imageId) { this.imageId = imageId; }
    /** @return product id */
    public int getProductId() { return productId; }
    /** @param productId product id */
    public void setProductId(int productId) { this.productId = productId; }
    /** @return image url */
    public String getImageUrl() { return imageUrl; }
    /** @param imageUrl image url */
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    /** @return sort order */
    public int getSortOrder() { return sortOrder; }
    /** @param sortOrder sort order */
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
