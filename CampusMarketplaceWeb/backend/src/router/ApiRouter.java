package router;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.AuthController;
import controller.CategoryController;
import controller.OrderController;
import controller.ProductController;
import controller.ProductImageController;
import controller.ProductCommentController;
import controller.SearchController;
import controller.FavoriteController;
import dto.AddProductRequest;
import dto.LoginRequest;
import dto.OrderRequest;
import dto.OrderResponse;
import dto.ProductDetailResponse;
import dto.ProductCommentResponse;
import dto.ProductResponse;
import dto.ApiResponse;
import dto.RegisterRequest;
import exception.AppException;
import exception.AuthException;
import exception.NotFoundException;
import filter.CorsFilter;
import model.Category;
import util.HttpUtil;
import util.JsonUtil;
import util.RequestUtil;
import util.ResponseUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * HTTP handler that routes API requests to controllers.
 */
public class ApiRouter implements HttpHandler {
    private final AuthController authController = new AuthController();
    private final ProductController productController = new ProductController();
    private final ProductImageController productImageController = new ProductImageController();
    private final ProductCommentController productCommentController = new ProductCommentController();
    private final CategoryController categoryController = new CategoryController();
    private final SearchController searchController = new SearchController();
    private final OrderController orderController = new OrderController();
    private final FavoriteController favoriteController = new FavoriteController();

    /**
     * Handles all /api requests.
     *
     * @param exchange exchange
     * @throws IOException io error
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        CorsFilter.apply(exchange);
        if (CorsFilter.isPreflight(exchange)) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        String path = exchange.getRequestURI().getPath();
        try {
            if ("/api/auth/register".equals(path)) {
                handleRegister(exchange); return;
            }
            if (path.startsWith("/api/product-images/")) {
                handleProductImageServe(exchange, path); return;
            }
            if ("/api/auth/login".equals(path)) {
                handleLogin(exchange); return;
            }
            if ("/api/products".equals(path) && HttpUtil.isMethod(exchange, "GET")) {
                handleProducts(exchange); return;
            }
            if ("/api/products".equals(path) && HttpUtil.isMethod(exchange, "POST")) {
                handleAddProduct(exchange); return;
            }
            if ("/api/products/image".equals(path) && HttpUtil.isMethod(exchange, "POST")) {
                handleUploadProductImage(exchange); return;
            }
            if ("/api/products/search".equals(path)) {
                handleSearchProducts(exchange); return;
            }
            if ("/api/products/my".equals(path)) {
                handleMyProducts(exchange); return;
            }
            if (path.startsWith("/api/products/") && path.endsWith("/comments")) {
                handleProductComments(exchange, path); return;
            }
            if (path.startsWith("/api/comments/")) {
                handleCommentDelete(exchange, path); return;
            }
            if (path.startsWith("/api/products/")) {
                handleProductDetail(exchange, path); return;
            }
            if ("/api/categories".equals(path)) {
                handleCategories(exchange); return;
            }
            if ("/api/orders/my".equals(path)) {
                handleMyOrders(exchange); return;
            }
            if ("/api/orders".equals(path)) {
                handleOrders(exchange); return;
            }
            if ("/api/search/hot-keywords".equals(path)) {
                handleHotKeywords(exchange); return;
            }
            if ("/api/favorites".equals(path) && HttpUtil.isMethod(exchange, "POST")) {
                handleAddFavorite(exchange); return;
            }
            if ("/api/favorites".equals(path) && HttpUtil.isMethod(exchange, "GET")) {
                handleListFavorites(exchange); return;
            }
            if ("/api/favorites".equals(path) && HttpUtil.isMethod(exchange, "DELETE")) {
                handleRemoveFavorite(exchange); return;
            }
            if ("/api/favorites/check".equals(path)) {
                handleCheckFavorite(exchange); return;
            }
            ResponseUtil.sendJson(exchange, 404, JsonUtil.fail("route not found"));
        } catch (AppException e) {
            ResponseUtil.sendJson(exchange, 400, JsonUtil.fail(e.getMessage()));
        } catch (Exception e) {
            ResponseUtil.sendJson(exchange, 500, JsonUtil.fail("internal server error"));
        }
    }

    private void handleRegister(HttpExchange exchange) throws IOException {
        if (!HttpUtil.isMethod(exchange, "POST")) { HttpUtil.methodNotAllowed(exchange); return; }
        String body = RequestUtil.readBody(exchange);
        RegisterRequest req = new RegisterRequest();
        req.setUsername(RequestUtil.getJsonString(body, "username"));
        req.setEmail(RequestUtil.getJsonString(body, "email"));
        req.setPassword(RequestUtil.getJsonString(body, "password"));
        ResponseUtil.sendJson(exchange, 200, JsonUtil.toJson(authController.register(req)));
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        if (!HttpUtil.isMethod(exchange, "POST")) { HttpUtil.methodNotAllowed(exchange); return; }
        String body = RequestUtil.readBody(exchange);
        LoginRequest req = new LoginRequest();
        req.setUsername(RequestUtil.getJsonString(body, "username"));
        req.setPassword(RequestUtil.getJsonString(body, "password"));
        ResponseUtil.sendJson(exchange, 200, JsonUtil.toJson(authController.login(req)));
    }

    private void handleProducts(HttpExchange exchange) throws IOException {
        List<ProductResponse> products = productController.listProducts();
        ResponseUtil.sendJson(exchange, 200, JsonUtil.successData(toProductsJson(products)));
    }

    private void handleSearchProducts(HttpExchange exchange) throws IOException {
        if (!HttpUtil.isMethod(exchange, "GET")) { HttpUtil.methodNotAllowed(exchange); return; }
        Map<String,String> q = RequestUtil.parseQuery(exchange.getRequestURI().getQuery());
        String keyword = q.get("keyword");
        List<ProductResponse> products = productController.searchProducts(null, keyword);
        ResponseUtil.sendJson(exchange, 200, JsonUtil.successData(toProductsJson(products)));
    }

    private void handleProductDetail(HttpExchange exchange, String path) throws IOException {
        if (!HttpUtil.isMethod(exchange, "GET")) { HttpUtil.methodNotAllowed(exchange); return; }
        String idPart = path.substring("/api/products/".length());
        int id = Integer.parseInt(idPart);
        ProductDetailResponse detail = productController.getProductDetail(id);
        String imgPart = detail.getImageUrl() == null ? "null" : "\"" + JsonUtil.escape(detail.getImageUrl()) + "\"";
        String json = "{\"productId\":" + detail.getProductId() + ",\"title\":\"" + JsonUtil.escape(detail.getTitle()) +
            "\",\"price\":" + detail.getPrice() + ",\"status\":\"" + JsonUtil.escape(detail.getStatus()) +
            "\",\"imageUrl\":" + imgPart + ",\"sellerId\":" + detail.getSellerId() + ",\"categoryId\":" + (detail.getCategoryId()==null?"null":detail.getCategoryId()) +
            ",\"description\":\"" + JsonUtil.escape(detail.getDescription()) + "\",\"searchHitCount\":" + detail.getSearchHitCount() + "}";
        ResponseUtil.sendJson(exchange, 200, JsonUtil.successData(json));
    }

    private void handleProductComments(HttpExchange exchange, String path) throws IOException {
        String idPart = path.substring("/api/products/".length(), path.length() - "/comments".length());
        int productId = Integer.parseInt(idPart);
        try {
            if (HttpUtil.isMethod(exchange, "GET")) {
                List<ProductCommentResponse> comments = productCommentController.listComments(productId);
                ResponseUtil.sendJson(exchange, 200, JsonUtil.successData(toCommentsJson(comments)));
                return;
            }
            if (HttpUtil.isMethod(exchange, "POST")) {
                String body = RequestUtil.readBody(exchange);
                int userId = defaultInt(RequestUtil.getJsonInt(body, "userId"), 0);
                String content = RequestUtil.getJsonString(body, "content");
                ResponseUtil.sendJson(exchange, 200, JsonUtil.toJson(productCommentController.addComment(productId, userId, content)));
                return;
            }
            HttpUtil.methodNotAllowed(exchange);
        } catch (NotFoundException e) {
            ResponseUtil.sendJson(exchange, 404, JsonUtil.fail(e.getMessage()));
        } catch (AppException e) {
            ResponseUtil.sendJson(exchange, 400, JsonUtil.fail(e.getMessage()));
        }
    }

    private void handleCommentDelete(HttpExchange exchange, String path) throws IOException {
        if (!HttpUtil.isMethod(exchange, "DELETE")) { HttpUtil.methodNotAllowed(exchange); return; }
        String idPart = path.substring("/api/comments/".length());
        int commentId = Integer.parseInt(idPart);
        Map<String, String> query = RequestUtil.parseQuery(exchange.getRequestURI().getQuery());
        int userId;
        try {
            userId = Integer.parseInt(query.getOrDefault("userId", "0"));
        } catch (NumberFormatException e) {
            userId = 0;
        }
        try {
            ResponseUtil.sendJson(exchange, 200, JsonUtil.toJson(productCommentController.deleteComment(commentId, userId)));
        } catch (NotFoundException e) {
            ResponseUtil.sendJson(exchange, 404, JsonUtil.fail(e.getMessage()));
        } catch (AuthException e) {
            ResponseUtil.sendJson(exchange, 403, JsonUtil.fail(e.getMessage()));
        } catch (AppException e) {
            ResponseUtil.sendJson(exchange, 400, JsonUtil.fail(e.getMessage()));
        }
    }

    private void handleAddProduct(HttpExchange exchange) throws IOException {
        String body = RequestUtil.readBody(exchange);
        AddProductRequest req = new AddProductRequest();
        req.setSellerId(defaultInt(RequestUtil.getJsonInt(body, "sellerId"), 0));
        req.setCategoryId(RequestUtil.getJsonInt(body, "categoryId"));
        req.setTitle(RequestUtil.getJsonString(body, "title"));
        req.setPrice(defaultInt(RequestUtil.getJsonInt(body, "price"), 0));
        req.setDescription(RequestUtil.getJsonString(body, "description"));
        ResponseUtil.sendJson(exchange, 200, JsonUtil.toJson(productController.addProduct(req)));
    }

    private void handleCategories(HttpExchange exchange) throws IOException {
        if (!HttpUtil.isMethod(exchange, "GET")) { HttpUtil.methodNotAllowed(exchange); return; }
        List<Category> cats = categoryController.listCategories();
        StringBuilder sb = new StringBuilder("[");
        for (int i=0;i<cats.size();i++) {
            Category c = cats.get(i);
            if (i>0) sb.append(',');
            sb.append("{\"categoryId\":").append(c.getCategoryId()).append(",\"name\":\"").append(JsonUtil.escape(c.getName())).append("\"}");
        }
        sb.append(']');
        ResponseUtil.sendJson(exchange, 200, JsonUtil.successData(sb.toString()));
    }

    private void handleMyProducts(HttpExchange exchange) throws IOException {
        if (!HttpUtil.isMethod(exchange, "GET")) { HttpUtil.methodNotAllowed(exchange); return; }
        Map<String, String> q = RequestUtil.parseQuery(exchange.getRequestURI().getQuery());
        String sid = q.get("sellerId");
        int sellerId;
        try {
            sellerId = Integer.parseInt(sid == null ? "0" : sid);
        } catch (NumberFormatException e) {
            ResponseUtil.sendJson(exchange, 200, JsonUtil.fail("Invalid sellerId"));
            return;
        }
        if (sellerId <= 0) {
            ResponseUtil.sendJson(exchange, 200, JsonUtil.fail("Invalid sellerId"));
            return;
        }
        List<ProductDetailResponse> products = productController.getMyProducts(sellerId);
        ResponseUtil.sendJson(exchange, 200, JsonUtil.successData(toProductsFullJson(products)));
    }

    private void handleOrders(HttpExchange exchange) throws IOException {
        if (!HttpUtil.isMethod(exchange, "POST")) { HttpUtil.methodNotAllowed(exchange); return; }
        String body = RequestUtil.readBody(exchange);
        OrderRequest req = new OrderRequest();
        req.setBuyerId(defaultInt(RequestUtil.getJsonInt(body, "buyerId"), 0));
        req.setProductId(defaultInt(RequestUtil.getJsonInt(body, "productId"), 0));
        ResponseUtil.sendJson(exchange, 200, JsonUtil.toJson(orderController.createOrder(req)));
    }

    private void handleMyOrders(HttpExchange exchange) throws IOException {
        if (!HttpUtil.isMethod(exchange, "GET")) { HttpUtil.methodNotAllowed(exchange); return; }
        Map<String, String> q = RequestUtil.parseQuery(exchange.getRequestURI().getQuery());
        int buyerId = Integer.parseInt(q.getOrDefault("buyerId", "0"));
        List<OrderResponse> orders = orderController.myOrders(buyerId);
        ResponseUtil.sendJson(exchange, 200, JsonUtil.successData(toOrdersJson(orders)));
    }

    private void handleHotKeywords(HttpExchange exchange) throws IOException {
        if (!HttpUtil.isMethod(exchange, "GET")) { HttpUtil.methodNotAllowed(exchange); return; }
        List<String> list = searchController.getHotKeywords();
        StringBuilder sb = new StringBuilder("[");
        for (int i=0;i<list.size();i++) {
            if (i>0) sb.append(',');
            sb.append("\"").append(JsonUtil.escape(list.get(i))).append("\"");
        }
        sb.append(']');
        ResponseUtil.sendJson(exchange, 200, JsonUtil.successData(sb.toString()));
    }

    private void handleAddFavorite(HttpExchange exchange) throws IOException {
        if (!HttpUtil.isMethod(exchange, "POST")) { HttpUtil.methodNotAllowed(exchange); return; }
        String body = RequestUtil.readBody(exchange);
        int userId = defaultInt(RequestUtil.getJsonInt(body, "userId"), 0);
        int productId = defaultInt(RequestUtil.getJsonInt(body, "productId"), 0);
        ResponseUtil.sendJson(exchange, 200, JsonUtil.toJson(favoriteController.addFavorite(userId, productId)));
    }

    private void handleRemoveFavorite(HttpExchange exchange) throws IOException {
        if (!HttpUtil.isMethod(exchange, "DELETE")) { HttpUtil.methodNotAllowed(exchange); return; }
        Map<String,String> q = RequestUtil.parseQuery(exchange.getRequestURI().getQuery());
        int userId = Integer.parseInt(q.getOrDefault("userId", "0"));
        int productId = Integer.parseInt(q.getOrDefault("productId", "0"));
        ResponseUtil.sendJson(exchange, 200, JsonUtil.toJson(favoriteController.removeFavorite(userId, productId)));
    }

    private void handleListFavorites(HttpExchange exchange) throws IOException {
        if (!HttpUtil.isMethod(exchange, "GET")) { HttpUtil.methodNotAllowed(exchange); return; }
        Map<String,String> q = RequestUtil.parseQuery(exchange.getRequestURI().getQuery());
        int userId = Integer.parseInt(q.getOrDefault("userId", "0"));
        List<dto.ProductResponse> products = favoriteController.listFavorites(userId);
        ResponseUtil.sendJson(exchange, 200, JsonUtil.successData(toProductsJson(products)));
    }

    private void handleCheckFavorite(HttpExchange exchange) throws IOException {
        if (!HttpUtil.isMethod(exchange, "GET")) { HttpUtil.methodNotAllowed(exchange); return; }
        Map<String,String> q = RequestUtil.parseQuery(exchange.getRequestURI().getQuery());
        int userId = Integer.parseInt(q.getOrDefault("userId", "0"));
        int productId = Integer.parseInt(q.getOrDefault("productId", "0"));
        ResponseUtil.sendJson(exchange, 200, JsonUtil.toJson(favoriteController.checkFavorite(userId, productId)));
    }

    private String toProductsJson(List<ProductResponse> products) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < products.size(); i++) {
            ProductResponse p = products.get(i);
            if (i > 0) sb.append(',');
            sb.append("{\"productId\":").append(p.getProductId())
                    .append(",\"title\":\"").append(JsonUtil.escape(p.getTitle()))
                    .append("\",\"price\":").append(p.getPrice())
                    .append(",\"status\":\"").append(JsonUtil.escape(p.getStatus())).append("\"")
                    .append(",\"imageUrl\":")
                    .append(p.getImageUrl() == null ? "null" : ("\"" + JsonUtil.escape(p.getImageUrl()) + "\""))
                    .append("}");
        }
        sb.append(']');
        return sb.toString();
    }

    private String toCommentsJson(List<ProductCommentResponse> comments) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < comments.size(); i++) {
            if (i > 0) sb.append(',');
            sb.append(productCommentController.toCommentJson(comments.get(i)));
        }
        sb.append(']');
        return sb.toString();
    }

    private String toProductsFullJson(List<ProductDetailResponse> products) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < products.size(); i++) {
            ProductDetailResponse p = products.get(i);
            if (i > 0) sb.append(',');
            sb.append('{')
                    .append("\"productId\":").append(p.getProductId())
                    .append(",\"title\":\"").append(JsonUtil.escape(p.getTitle())).append("\"")
                    .append(",\"price\":").append(p.getPrice())
                    .append(",\"status\":\"").append(JsonUtil.escape(p.getStatus())).append("\"")
                    .append(",\"imageUrl\":")
                    .append(p.getImageUrl() == null ? "null" : ("\"" + JsonUtil.escape(p.getImageUrl()) + "\""))
                    .append(",\"sellerId\":").append(p.getSellerId())
                    .append(",\"categoryId\":").append(p.getCategoryId() == null ? "null" : p.getCategoryId())
                    .append(",\"description\":\"").append(JsonUtil.escape(p.getDescription())).append("\"")
                    .append('}');
        }
        sb.append(']');
        return sb.toString();
    }

    private void handleUploadProductImage(HttpExchange exchange) throws IOException {
        if (!HttpUtil.isMethod(exchange, "POST")) { HttpUtil.methodNotAllowed(exchange); return; }
        String body = RequestUtil.readBody(exchange);
        int productId = defaultInt(RequestUtil.getJsonInt(body, "productId"), 0);
        String fileName = RequestUtil.getJsonString(body, "fileName");
        String mimeType = RequestUtil.getJsonString(body, "mimeType");
        String base64Data = RequestUtil.getJsonString(body, "base64Data");
        ApiResponse resp = productImageController.uploadImage(productId, fileName, mimeType, base64Data);
        ResponseUtil.sendJson(exchange, 200, JsonUtil.toJson(resp));
    }

    private void handleProductImageServe(HttpExchange exchange, String path) throws IOException {
        if (!HttpUtil.isMethod(exchange, "GET")) { HttpUtil.methodNotAllowed(exchange); return; }
        String fileName = path.substring("/api/product-images/".length());
        if (fileName.contains("..") || fileName.contains("/")) { ResponseUtil.sendJson(exchange, 404, JsonUtil.fail("not found")); return; }
        java.io.File f = new java.io.File("uploads/products", fileName);
        if (!f.exists() || !f.isFile()) { ResponseUtil.sendJson(exchange, 404, JsonUtil.fail("not found")); return; }
        String lower = fileName.toLowerCase();
        String ct = "application/octet-stream";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) ct = "image/jpeg";
        else if (lower.endsWith(".png")) ct = "image/png";
        else if (lower.endsWith(".webp")) ct = "image/webp";
        exchange.getResponseHeaders().set("Content-Type", ct);
        byte[] data = java.nio.file.Files.readAllBytes(f.toPath());
        exchange.sendResponseHeaders(200, data.length);
        try (java.io.OutputStream os = exchange.getResponseBody()) { os.write(data); }
    }

    private String toOrdersJson(List<OrderResponse> orders) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < orders.size(); i++) {
            OrderResponse o = orders.get(i);
            if (i > 0) {
                sb.append(',');
            }
            sb.append("{\"orderId\":").append(o.getOrderId())
                    .append(",\"productId\":").append(o.getProductId())
                    .append(",\"productTitle\":\"").append(JsonUtil.escape(o.getProductTitle())).append("\"")
                    .append(",\"price\":").append(o.getPrice())
                    .append(",\"sellerId\":").append(o.getSellerId())
                    .append(",\"buyerId\":").append(o.getBuyerId())
                    .append(",\"status\":\"").append(JsonUtil.escape(o.getStatus())).append("\"")
                    .append(",\"createdAt\":\"").append(JsonUtil.escape(o.getCreatedAt())).append("\"}");
        }
        sb.append(']');
        return sb.toString();
    }

    private int defaultInt(Integer v, int d) { return v == null ? d : v; }
}
