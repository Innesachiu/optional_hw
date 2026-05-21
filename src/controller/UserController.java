package controller;

import model.User;
import service.UserService;

/**
 * Controller for user actions.
 */
public class UserController {
    private final UserService userService = new UserService();

    public boolean register(String username, String password) {
        return userService.register(username, password);
    }

    public User login(String username, String password) {
        return userService.login(username, password);
    }
}
