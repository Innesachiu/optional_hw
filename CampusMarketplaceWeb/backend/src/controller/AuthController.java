package controller;

import dto.ApiResponse;
import dto.LoginRequest;
import dto.RegisterRequest;
import exception.AppException;
import model.User;
import service.AuthService;
import util.JsonUtil;

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
            ApiResponse resp = new ApiResponse(true, "Login successful");
            String dataJson = "{\"userId\":" + user.getUserId() + ",\"username\":\"" + JsonUtil.escape(user.getUsername()) + "\"}";
            resp.setData(dataJson);
            return resp;
        } catch (AppException e) {
            return new ApiResponse(false, e.getMessage());
        }
    }
}
