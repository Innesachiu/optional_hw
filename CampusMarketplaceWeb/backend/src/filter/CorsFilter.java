package filter;

import com.sun.net.httpserver.HttpExchange;

/**
 * Adds CORS headers and handles preflight checks.
 */
public final class CorsFilter {
    private CorsFilter() {
    }

    /**
     * Applies CORS headers.
     *
     * @param exchange exchange
     */
    public static void apply(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    /**
     * Checks if request is preflight OPTIONS.
     *
     * @param exchange exchange
     * @return true if OPTIONS
     */
    public static boolean isPreflight(HttpExchange exchange) {
        return "OPTIONS".equalsIgnoreCase(exchange.getRequestMethod());
    }
}
