package secretive.exception.throwable;

import secretive.exception.ValidationError;

import java.util.HashSet;

public class ValidationException extends RuntimeException{
    private HashSet<ValidationError> errors = new HashSet<>();

    public ValidationException() {

    }
    public ValidationException(ValidationError error) {
        errors.add(error);
    }
    public void add(ValidationError error) {
        errors.add(error);
    }
}
