package controller;

import model.Product;
import service.ProductService;

import java.util.List;

/**
 * Controller for product and search actions.
 */
public class ProductController {
    private final ProductService productService = new ProductService();

    public boolean addProduct(int sellerId, String title, String description, double price) {
        return productService.addProduct(sellerId, title, description, price);
    }

    public List<Product> browseActiveProducts() {
        return productService.browseActiveProducts();
    }

    public List<Product> searchProducts(Integer userId, String keyword) {
        return productService.searchProducts(userId, keyword);
    }

    public List<String> popularKeywords() {
        return productService.popularKeywords();
    }

    public Product productDetail(int id) {
        return productService.getProductDetail(id);
    }
}
