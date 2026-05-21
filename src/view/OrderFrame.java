package view;
import controller.OrderController;import model.Product;import model.User;import javax.swing.*;import java.awt.*;
/** Order confirm window. */
public class OrderFrame extends JFrame { private final OrderController controller=new OrderController();
    /** @param buyer buyer @param product product @param home home @param detail detail */
    public OrderFrame(User buyer,Product product,HomeFrame home,JFrame detail){setTitle("Order");setSize(360,180);setLocationRelativeTo(null);JPanel p=new JPanel(new BorderLayout());p.add(new JLabel("Confirm buy: "+product.getTitle()+" / "+product.getPrice()),BorderLayout.CENTER);JButton confirm=new JButton("Confirm Order");p.add(confirm,BorderLayout.SOUTH);add(p);confirm.addActionListener(e->{String msg=controller.placeOrder(buyer.getUserId(),product.getProductId());JOptionPane.showMessageDialog(this,msg);home.refreshList(new controller.ProductController().listActiveProducts());if(msg.contains("成功")){detail.dispose();dispose();}});}
}
