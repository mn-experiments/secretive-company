package secretive.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
    public static ValidationException from(ErrorMessage message, Object... parts) {
        return new ValidationException(message.getTemplate().formatted(parts));
    }
}
