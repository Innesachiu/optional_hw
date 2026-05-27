package controller;

import dto.AddProductRequest;
import dto.ApiResponse;
import dto.ProductDetailResponse;
import dto.ProductResponse;
import exception.AppException;
import service.ProductService;

import java.util.Collections;
import java.util.List;

/**
 * Controller for product APIs.
 */
public class ProductController {
    private final ProductService productService = new ProductService();

    /**
     * Handles GET /api/products.
     *
     * @return product list
     */
    public List<ProductResponse> listProducts() {
        return productService.listActiveProducts();
    }

    /**
     * Handles GET /api/products/search?keyword=xxx.
     *
     * @param userId user id nullable
     * @param keyword keyword
     * @return product list
     */
    public List<ProductResponse> searchProducts(Integer userId, String keyword) {
        try {
            return productService.searchProducts(userId, keyword);
        } catch (AppException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Handles GET /api/products/{id}.
     *
     * @param productId product id
     * @return product detail
     */
    public ProductDetailResponse getProductDetail(int productId) {
        return productService.getProductDetail(productId);
    }

    /**
     * Handles POST /api/products.
     *
     * @param request add product request
     * @return API response
     */
    public ApiResponse addProduct(AddProductRequest request) {
        try {
            productService.addProduct(request);
            return new ApiResponse(true, "product added");
        } catch (AppException e) {
            return new ApiResponse(false, e.getMessage());
        }
    }
}
