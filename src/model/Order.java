package model;
/** Order entity. */
public class Order { private int orderId,buyerId,sellerId,productId,amount; private String status;
/** Default constructor. */ public Order() {}
/** @return order id */ public int getOrderId(){return orderId;} /** @param v id */ public void setOrderId(int v){orderId=v;}
/** @return buyer id */ public int getBuyerId(){return buyerId;} /** @param v id */ public void setBuyerId(int v){buyerId=v;}
/** @return seller id */ public int getSellerId(){return sellerId;} /** @param v id */ public void setSellerId(int v){sellerId=v;}
/** @return product id */ public int getProductId(){return productId;} /** @param v id */ public void setProductId(int v){productId=v;}
/** @return amount */ public int getAmount(){return amount;} /** @param v amount */ public void setAmount(int v){amount=v;}
/** @return status */ public String getStatus(){return status;} /** @param v status */ public void setStatus(String v){status=v;}
}
