package router;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.AuthController;
import controller.CategoryController;
import controller.OrderController;
import controller.ProductController;
import controller.SearchController;
import dto.AddProductRequest;
import dto.LoginRequest;
import dto.OrderRequest;
import dto.OrderResponse;
import dto.ProductDetailResponse;
import dto.ProductResponse;
import dto.RegisterRequest;
import exception.AppException;
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
    private final CategoryController categoryController = new CategoryController();
    private final SearchController searchController = new SearchController();
    private final OrderController orderController = new OrderController();

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
            if ("/api/auth/login".equals(path)) {
                handleLogin(exchange); return;
            }
            if ("/api/products".equals(path) && HttpUtil.isMethod(exchange, "GET")) {
                handleProducts(exchange); return;
            }
            if ("/api/products".equals(path) && HttpUtil.isMethod(exchange, "POST")) {
                handleAddProduct(exchange); return;
            }
            if ("/api/products/search".equals(path)) {
                handleSearchProducts(exchange); return;
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
        String json = "{\"productId\":" + detail.getProductId() + ",\"title\":\"" + JsonUtil.escape(detail.getTitle()) +
                "\",\"price\":" + detail.getPrice() + ",\"status\":\"" + JsonUtil.escape(detail.getStatus()) +
                "\",\"sellerId\":" + detail.getSellerId() + ",\"categoryId\":" + (detail.getCategoryId()==null?"null":detail.getCategoryId()) +
                ",\"description\":\"" + JsonUtil.escape(detail.getDescription()) + "\",\"searchHitCount\":" + detail.getSearchHitCount() + "}";
        ResponseUtil.sendJson(exchange, 200, JsonUtil.successData(json));
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

    private String toProductsJson(List<ProductResponse> products) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < products.size(); i++) {
            ProductResponse p = products.get(i);
            if (i > 0) sb.append(',');
            sb.append("{\"productId\":").append(p.getProductId())
                    .append(",\"title\":\"").append(JsonUtil.escape(p.getTitle()))
                    .append("\",\"price\":").append(p.getPrice())
                    .append(",\"status\":\"").append(JsonUtil.escape(p.getStatus())).append("\"}");
        }
        sb.append(']');
        return sb.toString();
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
