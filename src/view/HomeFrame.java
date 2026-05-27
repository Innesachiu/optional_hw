package view;
import controller.ProductController;import model.Product;import model.User;import javax.swing.*;import java.awt.*;import java.util.*;
/** Home window. */
public class HomeFrame extends JFrame { private final User currentUser; private final ProductController controller=new ProductController(); private final DefaultListModel<String> model=new DefaultListModel<>(); private final java.util.List<Product> currentProducts=new ArrayList<>();
    /** @param user login user */
    public HomeFrame(User user){this.currentUser=user;setTitle("Home - "+user.getUsername());setSize(900,500);setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);setLocationRelativeTo(null);
        JPanel top=new JPanel();JTextField keyword=new JTextField(20);JButton search=new JButton("Search");JButton refresh=new JButton("Refresh");JButton add=new JButton("Add Product");JButton pop=new JButton("Popular");top.add(keyword);top.add(search);top.add(refresh);top.add(add);top.add(pop);
        JList<String> list=new JList<>(model);JButton detail=new JButton("View Detail");add(top,BorderLayout.NORTH);add(new JScrollPane(list),BorderLayout.CENTER);add(detail,BorderLayout.SOUTH);
        refreshList(controller.listActiveProducts());
        refresh.addActionListener(e->refreshList(controller.listActiveProducts()));
        search.addActionListener(e->refreshList(controller.searchProducts(currentUser.getUserId(),keyword.getText())));
        add.addActionListener(e->new AddProductFrame(this,currentUser).setVisible(true));
        pop.addActionListener(e->JOptionPane.showMessageDialog(this,String.join("\n",controller.popularKeywords())));
        detail.addActionListener(e->{int i=list.getSelectedIndex();if(i>=0)new ProductDetailFrame(currentUser,currentProducts.get(i),this).setVisible(true);});
    }
    /** Refreshes list content. @param products products */
    public void refreshList(java.util.List<Product> products){model.clear();currentProducts.clear();for(Product p:products){currentProducts.add(p);model.addElement(String.format("#%d | %s | %d | %s | %s | %s",p.getProductId(),p.getTitle(),p.getPrice(),p.getCategoryName(),p.getSellerName(),p.getStatus()));}}
}
