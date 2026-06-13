package exception;

/**
 * Exception for invalid input data.
 */
public class ValidationException extends AppException {
    /**
     * Creates validation exception.
     *
     * @param message error message
     */
    public ValidationException(String message) {
        super(message);
    }
}
