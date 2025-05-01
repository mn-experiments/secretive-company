package secretive.exception.throwable;

import secretive.exception.ErrorMessage;

public class ApiException extends RuntimeException {
    private int code;

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }
    public ApiException(ErrorMessage message, int code, Object... parts) {
        this(message.getTemplate().formatted(parts), code);
    }

    public int getCode() {
        return code;
    }
}
