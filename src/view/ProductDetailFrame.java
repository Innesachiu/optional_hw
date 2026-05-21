package view;
import controller.OrderController;import model.Product;import model.User;import javax.swing.*;import java.awt.*;
/** Product detail window. */
public class ProductDetailFrame extends JFrame { private final OrderController orderController=new OrderController();
    /** @param currentUser current user @param product product @param home home frame */
    public ProductDetailFrame(User currentUser,Product product,HomeFrame home){setTitle("Product Detail");setSize(500,350);setLocationRelativeTo(null);JTextArea area=new JTextArea();area.setEditable(false);area.setText("Title: "+product.getTitle()+"\nPrice: "+product.getPrice()+"\nDescription: "+product.getDescription()+"\nSeller: "+product.getSellerName()+"\nCategory: "+product.getCategoryName()+"\nStatus: "+product.getStatus());JButton buy=new JButton("Buy");add(new JScrollPane(area),BorderLayout.CENTER);add(buy,BorderLayout.SOUTH);buy.addActionListener(e->{new OrderFrame(currentUser,product,home,this).setVisible(true);});}
}
