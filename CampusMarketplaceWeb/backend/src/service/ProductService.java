package service;

import dao.CategoryDAO;
import dao.ProductDAO;
import dto.AddProductRequest;
import dto.ProductDetailResponse;
import dto.ProductResponse;
import exception.DatabaseException;
import exception.NotFoundException;
import exception.ValidationException;
import model.Category;
import model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles product business logic.
 */
public class ProductService {
    private final ProductDAO productDAO = new ProductDAO();
    private final SearchService searchService = new SearchService();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    /**
     * Gets active product list.
     *
     * @return product response list
     */
    public List<ProductResponse> listActiveProducts() {
        return toProductResponseList(productDAO.findActive());
    }

    /**
     * Searches active products and logs keyword.
     *
     * @param userId user id nullable
     * @param keyword keyword
     * @return product response list
     */
    public List<ProductResponse> searchProducts(Integer userId, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ValidationException("keyword is required");
        }
        searchService.logKeyword(userId, keyword);
        List<Product> list = productDAO.searchActive(keyword.trim());
        for (Product p : list) {
            productDAO.increaseSearchHit(p.getProductId());
        }
        return toProductResponseList(list);
    }

    /**
     * Gets product detail by id.
     *
     * @param productId product id
     * @return detail response
     */
    public ProductDetailResponse getProductDetail(int productId) {
        Product product = productDAO.findById(productId);
        if (product == null) {
            throw new NotFoundException("product not found");
        }
        ProductDetailResponse response = new ProductDetailResponse();
        response.setProductId(product.getProductId());
        response.setTitle(product.getTitle());
        response.setPrice(product.getPrice());
        response.setStatus(product.getStatus());
        response.setSellerId(product.getSellerId());
        response.setCategoryId(product.getCategoryId());
        response.setDescription(product.getDescription());
        response.setSearchHitCount(product.getSearchHitCount());
        return response;
    }

    /**
     * Adds a product.
     *
     * @param request add product request
     */
    public void addProduct(AddProductRequest request) {
        if (request == null || request.getSellerId() <= 0 || request.getPrice() <= 0 || isBlank(request.getTitle())) {
            throw new ValidationException("invalid product request");
        }
        Product product = new Product();
        product.setSellerId(request.getSellerId());
        product.setCategoryId(request.getCategoryId());
        product.setTitle(request.getTitle().trim());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        if (!productDAO.create(product)) {
            throw new DatabaseException("failed to add product");
        }
    }

    private List<ProductResponse> toProductResponseList(List<Product> products) {
        List<ProductResponse> list = new ArrayList<>();
        for (Product p : products) {
            ProductResponse r = new ProductResponse();
            r.setProductId(p.getProductId());
            r.setTitle(p.getTitle());
            r.setPrice(p.getPrice());
            r.setStatus(p.getStatus());
            list.add(r);
        }
        return list;
    }


    /**
     * Gets category list.
     *
     * @return category list
     */
    public List<Category> listCategories() {
        return categoryDAO.findAll();
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
