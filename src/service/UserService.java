package service;

import dao.UserDAO;
import model.User;

/**
 * Business service for user workflows.
 */
public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public boolean register(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }
        return userDAO.register(new User(0, username.trim(), password));
    }

    public User login(String username, String password) {
        return userDAO.login(username, password);
    }
}
