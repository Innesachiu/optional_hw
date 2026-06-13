package controller;

import dto.ApiResponse;
import dto.ProductResponse;
import exception.AppException;
import service.FavoriteService;

import java.util.Collections;
import java.util.List;

/**
 * Controller for favorites.
 */
public class FavoriteController {
    private final FavoriteService favoriteService = new FavoriteService();

    public ApiResponse addFavorite(int userId, int productId) {
        try {
            favoriteService.addFavorite(userId, productId);
            return new ApiResponse(true, "已加入我的最愛。");
        } catch (AppException e) {
            // if duplicate was handled as success in DAO, still return success message
            return new ApiResponse(false, e.getMessage());
        }
    }

    public ApiResponse removeFavorite(int userId, int productId) {
        try {
            favoriteService.removeFavorite(userId, productId);
            return new ApiResponse(true, "已從我的最愛移除。");
        } catch (AppException e) {
            return new ApiResponse(false, e.getMessage());
        }
    }

    public List<ProductResponse> listFavorites(int userId) {
        try {
            return favoriteService.getFavoritesByUser(userId);
        } catch (AppException e) {
            return Collections.emptyList();
        }
    }

    public ApiResponse checkFavorite(int userId, int productId) {
        try {
            boolean fav = favoriteService.exists(userId, productId);
            String json = "{\"favorited\":" + (fav ? "true" : "false") + "}";
            ApiResponse resp = new ApiResponse(true, "收藏狀態載入成功。");
            resp.setData(json);
            return resp;
        } catch (AppException e) {
            return new ApiResponse(false, e.getMessage());
        }
    }
}
