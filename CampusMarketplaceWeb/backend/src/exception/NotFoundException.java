package exception;

/**
 * Exception for missing resources.
 */
public class NotFoundException extends AppException {
    /**
     * Creates not found exception.
     *
     * @param message error message
     */
    public NotFoundException(String message) {
        super(message);
    }
}
