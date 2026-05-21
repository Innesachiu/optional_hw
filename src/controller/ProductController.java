package controller;
import model.Category;import model.Product;import service.*;import java.util.*;
/** Controller for products. */
public class ProductController { private final ProductService p=new ProductService(); private final SearchService s=new SearchService();
    /** @return active list */ public List<Product> listActiveProducts(){return p.getActiveProducts();}
    /** @param uid user id @param kw keyword @return result */ public List<Product> searchProducts(Integer uid,String kw){return p.searchProducts(uid,kw,s);} 
    /** @return popular keywords */ public List<String> popularKeywords(){return s.popularKeywords();}
    /** @param seller seller @param cat category @param title title @param price price @param desc desc @return success */ public boolean addProduct(int seller,int cat,String title,int price,String desc){return p.addProduct(seller,cat,title,price,desc);} 
    /** @param id product id @return product */ public Product getProductDetail(int id){return p.getProductDetail(id);} 
    /** @return category list */ public List<Category> listCategories(){return p.getCategories();}
}
