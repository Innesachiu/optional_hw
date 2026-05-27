package controller;

import model.Category;
import service.ProductService;

import java.util.List;

/**
 * Controller for category APIs.
 */
public class CategoryController {
    private final ProductService productService = new ProductService();

    /**
     * Handles GET /api/categories.
     *
     * @return category list
     */
    public List<Category> listCategories() {
        return productService.listCategories();
    }
}
