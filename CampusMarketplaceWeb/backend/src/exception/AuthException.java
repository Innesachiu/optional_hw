package exception;

/**
 * Exception for authentication failures.
 */
public class AuthException extends AppException {
    /**
     * Creates auth exception.
     *
     * @param message error message
     */
    public AuthException(String message) {
        super(message);
    }
}
