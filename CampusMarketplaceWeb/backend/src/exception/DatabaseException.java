package exception;

/**
 * Exception for persistence errors.
 */
public class DatabaseException extends AppException {
    /**
     * Creates database exception.
     *
     * @param message error message
     */
    public DatabaseException(String message) {
        super(message);
    }
}
