package service;

import dao.FavoriteDAO;
import dao.ProductDAO;
import dao.ProductImageDAO;
import dto.ProductResponse;
import exception.AppException;
import model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for favorites feature.
 */
public class FavoriteService {
    private final FavoriteDAO favoriteDAO = new FavoriteDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final ProductImageDAO productImageDAO = new ProductImageDAO();

    public boolean addFavorite(int userId, int productId) throws AppException {
        if (userId <= 0 || productId <= 0) throw new AppException("Invalid userId or productId");
        Product p = productDAO.findById(productId);
        if (p == null) throw new AppException("商品不存在。");
        boolean ok = favoriteDAO.addFavorite(userId, productId);
        if (!ok) throw new AppException("無法加入我的最愛。請稍後再試。");
        return true;
    }

    public boolean removeFavorite(int userId, int productId) throws AppException {
        if (userId <= 0 || productId <= 0) throw new AppException("Invalid userId or productId");
        boolean ok = favoriteDAO.removeFavorite(userId, productId);
        if (!ok) throw new AppException("無法從我的最愛移除。請稍後再試。");
        return true;
    }

    public boolean exists(int userId, int productId) throws AppException {
        if (userId <= 0 || productId <= 0) throw new AppException("Invalid userId or productId");
        return favoriteDAO.exists(userId, productId);
    }

    public List<ProductResponse> getFavoritesByUser(int userId) throws AppException {
        if (userId <= 0) throw new AppException("Invalid userId");
        List<Product> products = favoriteDAO.findProductsByUserId(userId);
        List<ProductResponse> out = new ArrayList<>();
        for (Product p : products) {
            ProductResponse r = new ProductResponse();
            r.setProductId(p.getProductId());
            r.setTitle(p.getTitle());
            r.setImageUrl(productImageDAO.findPrimaryImageUrl(p.getProductId()));
            r.setPrice(p.getPrice());
            r.setStatus(p.getStatus());
            out.add(r);
        }
        return out;
    }
}
