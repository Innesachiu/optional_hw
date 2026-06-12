package util;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Request parsing utility.
 */
public final class RequestUtil {
    private RequestUtil() {
    }

    /**
     * Reads request body as UTF-8 string.
     *
     * @param exchange exchange
     * @return request body string
     * @throws IOException io error
     */
    public static String readBody(HttpExchange exchange) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    /**
     * Parses query string to map.
     *
     * @param query query string
     * @return key-value map
     */
    public static Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return map;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String value = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            map.put(key, value);
        }
        return map;
    }

    /**
     * Extracts a JSON string value by key (simple parser).
     *
     * @param json json text
     * @param key key
     * @return value or null
     */
    public static String getJsonString(String json, String key) {
        String pattern = "\"" + key + "\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) return null;
        int colon = json.indexOf(':', idx);
        int firstQuote = json.indexOf('"', colon + 1);
        int secondQuote = json.indexOf('"', firstQuote + 1);
        if (colon < 0 || firstQuote < 0 || secondQuote < 0) return null;
        return json.substring(firstQuote + 1, secondQuote);
    }

    /**
     * Extracts a JSON integer value by key (simple parser).
     *
     * @param json json text
     * @param key key
     * @return integer or null
     */
    public static Integer getJsonInt(String json, String key) {
        String pattern = "\"" + key + "\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) return null;
        int colon = json.indexOf(':', idx);
        if (colon < 0) return null;
        int end = colon + 1;
        while (end < json.length() && " -0123456789".indexOf(json.charAt(end)) >= 0) end++;
        String raw = json.substring(colon + 1, end).trim();
        if (raw.isEmpty()) return null;
        try { return Integer.parseInt(raw); } catch (NumberFormatException e) { return null; }
    }
}
