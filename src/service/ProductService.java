package service;

import dao.ProductDAO;
import dao.SearchLogDAO;
import model.Product;

import java.util.List;

/**
 * Business service for product workflows.
 */
public class ProductService {
    private final ProductDAO productDAO = new ProductDAO();
    private final SearchLogDAO searchLogDAO = new SearchLogDAO();

    public boolean addProduct(int sellerId, String title, String description, double price) {
        Product product = new Product(0, sellerId, title, description, price, "ACTIVE");
        return productDAO.addProduct(product);
    }

    public List<Product> browseActiveProducts() {
        return productDAO.getActiveProducts();
    }

    public List<Product> searchProducts(Integer userId, String keyword) {
        searchLogDAO.logSearch(userId, keyword);
        return productDAO.searchProducts(keyword);
    }

    public List<String> popularKeywords() {
        return searchLogDAO.getPopularKeywordsLast7Days();
    }

    public Product getProductDetail(int id) {
        return productDAO.getProductById(id);
    }

    public boolean markAsSold(int productId) {
        return productDAO.markAsSold(productId);
    }
}
