package util;

/**
 * Minimal JSON utility for simple API response generation.
 */
public final class JsonUtil {
    private JsonUtil() {
    }

    /**
     * Escapes text for safe JSON output.
     *
     * @param value input text
     * @return escaped text
     */
    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    /**
     * Builds a simple JSON message response.
     *
     * @param success success flag
     * @param message response message
     * @return JSON string
     */
    public static String message(boolean success, String message) {
        return "{\"success\":" + success + ",\"message\":\"" + escape(message) + "\"}";
    }
}
