package service;

import dao.UserDAO;
import model.User;
import util.PasswordUtil;

/**
 * Business service for user workflows.
 */
public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public boolean register(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }
        String hashed = PasswordUtil.hash(password);
        return userDAO.register(new User(0, username.trim(), hashed));
    }

    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        return userDAO.login(username.trim(), PasswordUtil.hash(password));
    }
}
