package util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

/**
 * HTTP utility methods for common checks.
 */
public final class HttpUtil {
    private HttpUtil() {
    }

    /**
     * Checks if HTTP method matches expected value.
     *
     * @param exchange exchange
     * @param method expected method
     * @return true when method matches
     */
    public static boolean isMethod(HttpExchange exchange, String method) {
        return method.equalsIgnoreCase(exchange.getRequestMethod());
    }

    /**
     * Sends 405 JSON response for unsupported methods.
     *
     * @param exchange exchange
     * @throws IOException io error
     */
    public static void methodNotAllowed(HttpExchange exchange) throws IOException {
        ResponseUtil.sendJson(exchange, 405, JsonUtil.fail("method not allowed"));
    }
}
