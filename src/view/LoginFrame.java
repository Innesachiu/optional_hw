package view;
import controller.UserController;import model.User;import javax.swing.*;import java.awt.*;
/** Login window. */
public class LoginFrame extends JFrame { private final UserController controller=new UserController();
    /** Builds login frame. */
    public LoginFrame(){setTitle("Login");setSize(360,220);setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);setLocationRelativeTo(null);JPanel p=new JPanel(new GridLayout(4,2,8,8));JTextField user=new JTextField();JPasswordField pass=new JPasswordField();JButton login=new JButton("Login");JButton reg=new JButton("Go Register");p.add(new JLabel("Username"));p.add(user);p.add(new JLabel("Password"));p.add(pass);p.add(login);p.add(reg);add(p);
        login.addActionListener(e->{User u=controller.login(user.getText(),new String(pass.getPassword()));if(u!=null){new HomeFrame(u).setVisible(true);dispose();}else JOptionPane.showMessageDialog(this,"登入失敗");});
        reg.addActionListener(e->{new RegisterFrame(this).setVisible(true);setVisible(false);});}
}
