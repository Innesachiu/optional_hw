package model;
/** Product entity. */
public class Product { private int productId,sellerId,categoryId,price,searchHitCount; private String title,description,status,sellerName,categoryName;
/** Default constructor. */ public Product() {}
/** @return product id */ public int getProductId(){return productId;} /** @param v id */ public void setProductId(int v){productId=v;}
/** @return seller id */ public int getSellerId(){return sellerId;} /** @param v seller id */ public void setSellerId(int v){sellerId=v;}
/** @return category id */ public int getCategoryId(){return categoryId;} /** @param v category id */ public void setCategoryId(int v){categoryId=v;}
/** @return price */ public int getPrice(){return price;} /** @param v price */ public void setPrice(int v){price=v;}
/** @return title */ public String getTitle(){return title;} /** @param v title */ public void setTitle(String v){title=v;}
/** @return description */ public String getDescription(){return description;} /** @param v description */ public void setDescription(String v){description=v;}
/** @return status */ public String getStatus(){return status;} /** @param v status */ public void setStatus(String v){status=v;}
/** @return hit count */ public int getSearchHitCount(){return searchHitCount;} /** @param v hit */ public void setSearchHitCount(int v){searchHitCount=v;}
/** @return seller name */ public String getSellerName(){return sellerName;} /** @param v seller */ public void setSellerName(String v){sellerName=v;}
/** @return category name */ public String getCategoryName(){return categoryName;} /** @param v category */ public void setCategoryName(String v){categoryName=v;}
}
