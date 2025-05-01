package secretive.exception;

public record ValidationError(String message) {
    public static ValidationError from(ErrorMessage message, Object... parts) {
        return new ValidationError(message.getTemplate().formatted(parts));
    }
}
