package exception;

/**
 * Base runtime exception for application-level errors.
 */
public class AppException extends RuntimeException {
    /**
     * Creates an exception with message.
     *
     * @param message error message
     */
    public AppException(String message) {
        super(message);
    }
}
