package model;
/** Product image entity. */
public class ProductImage { private int imageId,productId,sortOrder; private String imageUrl;
/** Default constructor. */ public ProductImage() {}
/** @return image id */ public int getImageId(){return imageId;} /** @param v id */ public void setImageId(int v){imageId=v;}
/** @return product id */ public int getProductId(){return productId;} /** @param v id */ public void setProductId(int v){productId=v;}
/** @return image url */ public String getImageUrl(){return imageUrl;} /** @param v url */ public void setImageUrl(String v){imageUrl=v;}
/** @return order */ public int getSortOrder(){return sortOrder;} /** @param v order */ public void setSortOrder(int v){sortOrder=v;}
}
