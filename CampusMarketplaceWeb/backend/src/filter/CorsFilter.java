package filter;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

/**
 * Utility for adding CORS headers.
 */
public class CorsFilter {
    /**
     * Adds CORS headers to every response.
     *
     * @param exchange HTTP exchange
     */
    public static void apply(HttpExchange exchange) {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        headers.set("Access-Control-Max-Age", "3600");
    }

    /**
     * Checks whether the request is a CORS preflight request.
     *
     * @param exchange HTTP exchange
     * @return true if request method is OPTIONS
     */
    public static boolean isPreflight(HttpExchange exchange) {
        return "OPTIONS".equalsIgnoreCase(exchange.getRequestMethod());
    }
}