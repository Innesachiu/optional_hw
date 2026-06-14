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
            int productId = productService.addProduct(request);
            ApiResponse resp = new ApiResponse(true, "商品上架成功。");
            resp.setData("{\"productId\":" + productId + "}");
            return resp;
        } catch (AppException e) {
            return new ApiResponse(false, e.getMessage());
        }
    }

    /**
     * Handles GET /api/products/my?sellerId=xxx
     *
     * @param sellerId seller id
     * @return product detail list
     */
    public List<ProductDetailResponse> getMyProducts(int sellerId) {
        try {
            return productService.getProductsBySellerId(sellerId);
        } catch (AppException e) {
            return Collections.emptyList();
        }
    }
}
