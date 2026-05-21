import javax.swing.SwingUtilities;import view.LoginFrame;
/** Application entry point. */
public class Main {
    /** Starts application. @param args cli args */
    public static void main(String[] args){SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));}
}
