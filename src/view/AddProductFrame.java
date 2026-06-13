package view;
import controller.ProductController;import model.Category;import model.User;import javax.swing.*;import java.awt.*;import java.util.*;
/** Add product window. */
public class AddProductFrame extends JFrame { private final ProductController controller=new ProductController();
    /** @param home home frame @param user login user */
    public AddProductFrame(HomeFrame home,User user){setTitle("Add Product");setSize(450,300);setLocationRelativeTo(null);JPanel p=new JPanel(new GridLayout(6,2,8,8));JTextField title=new JTextField();JTextField price=new JTextField();JTextArea desc=new JTextArea();java.util.List<Category> cats=controller.listCategories();JComboBox<String> cbox=new JComboBox<>();for(Category c:cats)cbox.addItem(c.getCategoryId()+" - "+c.getName());JButton save=new JButton("Save");p.add(new JLabel("Title"));p.add(title);p.add(new JLabel("Price"));p.add(price);p.add(new JLabel("Category"));p.add(cbox);p.add(new JLabel("Description"));p.add(new JScrollPane(desc));p.add(save);add(p);
        save.addActionListener(e->{try{int idx=cbox.getSelectedIndex();int catId=cats.get(idx).getCategoryId();boolean ok=controller.addProduct(user.getUserId(),catId,title.getText(),Integer.parseInt(price.getText()),desc.getText());JOptionPane.showMessageDialog(this,ok?"新增成功":"新增失敗");if(ok){home.refreshList(controller.listActiveProducts());dispose();}}catch(Exception ex){JOptionPane.showMessageDialog(this,"資料格式錯誤");}});
    }
}
