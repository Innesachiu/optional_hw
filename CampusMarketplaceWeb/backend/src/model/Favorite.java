package model;

import java.sql.Timestamp;

/**
 * Favorite entity mapping favorites table.
 */
public class Favorite {
    private int favoriteId;
    private int userId;
    private int productId;
    private Timestamp createdAt;

    public Favorite() {}
    public int getFavoriteId() { return favoriteId; }
    public void setFavoriteId(int favoriteId) { this.favoriteId = favoriteId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
