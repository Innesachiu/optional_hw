package util;

import dto.ApiResponse;

/**
 * JSON utility for building simple JSON payloads.
 */
public final class JsonUtil {
    private JsonUtil() {
    }

    /**
     * Escapes plain text to safe JSON text.
     *
     * @param value source text
     * @return escaped text
     */
    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    /**
     * Converts ApiResponse to JSON.
     *
     * @param response response
     * @return json string
     */
    public static String toJson(ApiResponse response) {
        return "{\"success\":" + response.isSuccess() + ",\"message\":\"" + escape(response.getMessage()) + "\"}";
    }

    /**
     * Wraps raw data JSON into success response object.
     *
     * @param dataJson raw json
     * @return wrapped json
     */
    public static String successData(String dataJson) {
        return "{\"success\":true,\"data\":" + dataJson + "}";
    }

    /**
     * Builds fail JSON object.
     *
     * @param message message
     * @return fail json
     */
    public static String fail(String message) {
        return "{\"success\":false,\"message\":\"" + escape(message) + "\"}";
    }
}
