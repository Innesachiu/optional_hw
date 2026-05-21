package service;
import dao.OrderDAO;import model.Order;import model.Product;
/** Service for orders. */
public class OrderService { private final OrderDAO orderDAO=new OrderDAO(); private final ProductService productService=new ProductService();
    /** @param buyerId buyer @param productId product @return message */
    public String placeOrder(int buyerId,int productId){Product p=productService.getProductDetail(productId);if(p==null)return "商品不存在";if(!"ACTIVE".equals(p.getStatus()))return "商品已售出";if(p.getSellerId()==buyerId)return "不能購買自己的商品";Order o=new Order();o.setBuyerId(buyerId);o.setSellerId(p.getSellerId());o.setProductId(productId);o.setAmount(p.getPrice());o.setStatus("COMPLETED");if(!orderDAO.createOrder(o))return "建立訂單失敗";if(!productService.markSold(productId))return "訂單已建立，但商品狀態更新失敗";return "下訂單成功";}
}
