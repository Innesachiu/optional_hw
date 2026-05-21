package service;
import dao.CategoryDAO;import dao.ProductDAO;import model.Category;import model.Product;import java.util.*;
/** Service for products. */
public class ProductService { private final ProductDAO dao=new ProductDAO(); private final CategoryDAO categoryDAO=new CategoryDAO();
    /** @return active list */ public List<Product> getActiveProducts(){return dao.findActiveProducts();}
    /** @param userId user id @param keyword keyword @param searchService search service @return result */
    public List<Product> searchProducts(Integer userId,String keyword,SearchService searchService){if(keyword==null||keyword.trim().isEmpty())return new ArrayList<>();searchService.logSearch(userId,keyword.trim());List<Product> list=dao.searchActiveProducts(keyword.trim());for(Product p:list)dao.increaseSearchHit(p.getProductId());return list;}
    /** @param sellerId seller @param categoryId category @param title title @param price price @param description desc @return success */
    public boolean addProduct(int sellerId,int categoryId,String title,int price,String description){if(title==null||title.trim().isEmpty()||price<=0)return false;Product p=new Product();p.setSellerId(sellerId);p.setCategoryId(categoryId);p.setTitle(title.trim());p.setPrice(price);p.setDescription(description);return dao.insertProduct(p);} 
    /** @param productId id @return product */ public Product getProductDetail(int productId){return dao.findById(productId);} 
    /** @param productId id @return success */ public boolean markSold(int productId){return dao.markSold(productId);} 
    /** @return categories */ public List<Category> getCategories(){return categoryDAO.findAll();}
}
