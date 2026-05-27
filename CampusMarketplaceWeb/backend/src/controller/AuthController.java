package controller;

import dto.ApiResponse;
import dto.LoginRequest;
import dto.RegisterRequest;
import exception.AppException;
import model.User;
import service.AuthService;

/**
 * Controller for auth APIs.
 */
public class AuthController {
    private final AuthService authService = new AuthService();

    /**
     * Handles POST /api/auth/register.
     *
     * @param request register request
     * @return API response
     */
    public ApiResponse register(RegisterRequest request) {
        try {
            User user = authService.register(request);
            return new ApiResponse(true, "register success: " + user.getUsername());
        } catch (AppException e) {
            return new ApiResponse(false, e.getMessage());
        }
    }

    /**
     * Handles POST /api/auth/login.
     *
     * @param request login request
     * @return API response
     */
    public ApiResponse login(LoginRequest request) {
        try {
            User user = authService.login(request);
            return new ApiResponse(true, "login success: " + user.getUsername());
        } catch (AppException e) {
            return new ApiResponse(false, e.getMessage());
        }
    }
}
