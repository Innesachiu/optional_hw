package view;
import controller.UserController;import javax.swing.*;import java.awt.*;
/** Register window. */
public class RegisterFrame extends JFrame { private final UserController controller=new UserController();
    /** @param loginFrame parent login frame */
    public RegisterFrame(LoginFrame loginFrame){setTitle("Register");setSize(420,260);setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);setLocationRelativeTo(null);JPanel p=new JPanel(new GridLayout(5,2,8,8));JTextField u=new JTextField();JTextField e=new JTextField();JPasswordField pw=new JPasswordField();JButton ok=new JButton("Register");JButton back=new JButton("Back");p.add(new JLabel("Username"));p.add(u);p.add(new JLabel("Email"));p.add(e);p.add(new JLabel("Password"));p.add(pw);p.add(ok);p.add(back);add(p);
        ok.addActionListener(x->{boolean done=controller.register(u.getText(),e.getText(),new String(pw.getPassword()));JOptionPane.showMessageDialog(this,done?"註冊成功":"註冊失敗，請檢查欄位或重複帳號");if(done){dispose();loginFrame.setVisible(true);}});
        back.addActionListener(x->{dispose();loginFrame.setVisible(true);});}
}
