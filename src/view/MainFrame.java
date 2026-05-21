package view;

import controller.OrderController;
import controller.ProductController;
import controller.UserController;
import model.Product;
import model.PurchaseResult;
import model.User;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

/**
 * Basic Swing UI for marketplace demo flows.
 */
public class MainFrame extends JFrame {
    private final UserController userController = new UserController();
    private final ProductController productController = new ProductController();
    private final OrderController orderController = new OrderController();

    private User currentUser;
    private final JTextArea outputArea = new JTextArea(18, 60);

    public MainFrame() {
        setTitle("Campus Second-hand Marketplace");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(2, 5, 5, 5));
        addButton(buttonPanel, "Register", this::register);
        addButton(buttonPanel, "Login", this::login);
        addButton(buttonPanel, "Browse", this::browse);
        addButton(buttonPanel, "Search", this::search);
        addButton(buttonPanel, "Popular", this::popularKeywords);
        addButton(buttonPanel, "Add Product", this::addProduct);
        addButton(buttonPanel, "Detail", this::detail);
        addButton(buttonPanel, "Purchase", this::purchase);

        outputArea.setEditable(false);
        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private void addButton(JPanel panel, String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        panel.add(button);
    }

    private void register() {
        String username = JOptionPane.showInputDialog(this, "Username:");
        String password = JOptionPane.showInputDialog(this, "Password:");
        boolean ok = userController.register(username, password);
        append(ok ? "Register success" : "Register failed (username duplicated or invalid input)");
    }

    private void login() {
        String username = JOptionPane.showInputDialog(this, "Username:");
        String password = JOptionPane.showInputDialog(this, "Password:");
        currentUser = userController.login(username, password);
        append(currentUser != null ? "Login success: " + currentUser.getUsername() : "Login failed");
    }

    private void browse() {
        List<Product> products = productController.browseActiveProducts();
        append("=== Active Products ===");
        if (products.isEmpty()) {
            append("No active products");
        }
        for (Product p : products) {
            append(formatProduct(p));
        }
    }

    private void search() {
        String keyword = JOptionPane.showInputDialog(this, "Keyword:");
        if (keyword == null || keyword.trim().isEmpty()) {
            append("Search skipped: empty keyword");
            return;
        }
        Integer uid = currentUser == null ? null : currentUser.getId();
        List<Product> products = productController.searchProducts(uid, keyword.trim());
        append("=== Search: " + keyword + " ===");
        if (products.isEmpty()) {
            append("No matching products");
        }
        for (Product p : products) {
            append(formatProduct(p));
        }
    }

    private void popularKeywords() {
        List<String> list = productController.popularKeywords();
        append("=== Popular Keywords(7 days) ===");
        if (list.isEmpty()) {
            append("No keywords yet");
        }
        for (String s : list) {
            append(s);
        }
    }

    private void addProduct() {
        if (currentUser == null) {
            append("Please login first.");
            return;
        }
        String title = JOptionPane.showInputDialog(this, "Title:");
        String description = JOptionPane.showInputDialog(this, "Description:");
        String priceText = JOptionPane.showInputDialog(this, "Price:");
        if (title == null || title.trim().isEmpty() || priceText == null) {
            append("Add product failed: invalid input");
            return;
        }
        try {
            double price = Double.parseDouble(priceText);
            boolean ok = productController.addProduct(currentUser.getId(), title.trim(), description, price);
            append(ok ? "Add product success" : "Add product failed");
        } catch (NumberFormatException e) {
            append("Add product failed: price must be a number");
        }
    }

    private void detail() {
        String idText = JOptionPane.showInputDialog(this, "Product ID:");
        try {
            Product p = productController.productDetail(Integer.parseInt(idText));
            append(p == null ? "Product not found" : "Detail: " + formatProduct(p));
        } catch (Exception e) {
            append("Detail failed: invalid product id");
        }
    }

    private void purchase() {
        if (currentUser == null) {
            append("Please login first.");
            return;
        }
        String idText = JOptionPane.showInputDialog(this, "Product ID:");
        try {
            PurchaseResult result = orderController.createOrder(Integer.parseInt(idText), currentUser.getId());
            append("Purchase result: " + result.name());
        } catch (Exception e) {
            append("Purchase failed: invalid product id");
        }
    }

    private String formatProduct(Product p) {
        return String.format("#%d | seller:%d | %s | $%.2f | %s", p.getId(), p.getSellerId(), p.getTitle(), p.getPrice(), p.getStatus());
    }

    private void append(String text) {
        outputArea.append(text + "\n");
    }
}
