package service;

import dao.UserDAO;
import dto.LoginRequest;
import dto.RegisterRequest;
import exception.AuthException;
import exception.DatabaseException;
import exception.ValidationException;
import model.User;
import util.PasswordUtil;

/**
 * Handles register/login business logic.
 */
public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    /**
     * Registers a new user.
     *
     * @param request register request
     * @return created user basic data
     */
    public User register(RegisterRequest request) {
        validateRegister(request);
        User exists = userDAO.findByUsername(request.getUsername());
        if (exists != null) {
            throw new ValidationException("username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim());
        user.setPasswordHash(PasswordUtil.hash(request.getPassword()));

        if (!userDAO.create(user)) {
            throw new DatabaseException("failed to create user");
        }
        return userDAO.findByUsername(user.getUsername());
    }

    /**
     * Authenticates user by username and password.
     *
     * @param request login request
     * @return matched user
     */
    public User login(LoginRequest request) {
        if (request == null || isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            throw new ValidationException("username and password are required");
        }
        User user = userDAO.findByUsername(request.getUsername().trim());
        if (user == null) {
            throw new AuthException("invalid username or password");
        }
        String hashed = PasswordUtil.hash(request.getPassword());
        if (!hashed.equals(user.getPasswordHash())) {
            throw new AuthException("invalid username or password");
        }
        return user;
    }

    /**
     * Validates register request fields.
     *
     * @param request register request
     */
    public void validateRegister(RegisterRequest request) {
        if (request == null) {
            throw new ValidationException("request is required");
        }
        if (isBlank(request.getUsername()) || isBlank(request.getEmail()) || isBlank(request.getPassword())) {
            throw new ValidationException("username, email, password are required");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
