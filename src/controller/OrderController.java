package controller;
import service.OrderService;
/** Controller for orders. */
public class OrderController { private final OrderService s=new OrderService();
    /** @param buyerId buyer @param productId product @return message */ public String placeOrder(int buyerId,int productId){return s.placeOrder(buyerId,productId);} }
