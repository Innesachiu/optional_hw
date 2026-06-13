package dto;

/**
 * Standard API response object.
 */
public class ApiResponse {
    private boolean success;
    private String message;
    private String data;

    /** Default constructor. */
    public ApiResponse() {}
    /**
     * Creates response object.
     *
     * @param success success flag
     * @param message message
     */
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /** @return data JSON fragment or null */
    public String getData() { return data; }
    /** @param data JSON fragment string */
    public void setData(String data) { this.data = data; }

    /** @return success */
    public boolean isSuccess() { return success; }
    /** @param success success */
    public void setSuccess(boolean success) { this.success = success; }
    /** @return message */
    public String getMessage() { return message; }
    /** @param message message */
    public void setMessage(String message) { this.message = message; }
}
