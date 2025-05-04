package secretive.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import secretive.exception.throwable.ApiException;

import java.util.ArrayList;
import java.util.Optional;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ErrorResponse> processUnhandledException(RuntimeException e) {
        var constraintViolation = findConstraintViolation(e);

        if (constraintViolation.isPresent()) {
            return processBeanValidationException(constraintViolation.get());
        }
        return new ResponseEntity<>(new ErrorResponse("internal server error"), HttpStatusCode.valueOf(500));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ErrorResponse> processDatabaseException(DataIntegrityViolationException e) {
        var message = e.getMessage();
        if (message.contains("violates foreign key constraint")) {
            var cause = figureOutForeignKeyErrorCause(message, e);

            return new ResponseEntity<>(new ErrorResponse(cause), HttpStatusCode.valueOf(400));
        }

        throw new RuntimeException("could not hande DB exception", e);
    }

    private String figureOutForeignKeyErrorCause(String msg, DataIntegrityViolationException e) {
        var details = msg.split("Detail: ")[1];
        var detailsEndIndex = details.indexOf("]");
        var relevantDetails = details.substring(0, detailsEndIndex);
        if (relevantDetails.contains("is not present in table")) {
            var afterFirstQuote = relevantDetails.indexOf("\"") + 1;
            var beforeSecondQuote = relevantDetails.indexOf("\"", afterFirstQuote + 1);
            var foreignKeyReferencedObject = relevantDetails.substring(afterFirstQuote, beforeSecondQuote);

            return "failed to create because the linked [%s] does not exist".formatted(foreignKeyReferencedObject);
        }
        throw new RuntimeException("could not hande DB exception", e);
    }

    @ExceptionHandler(ApiException.class)
    ResponseEntity<ErrorResponse> processApiException(ApiException e) {
        var response = new ErrorResponse(e.getMessage());

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(e.getCode()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorResponse> processBeanValidationException(ConstraintViolationException e) {
        var messages = new ArrayList<String>();
        for (var x : e.getConstraintViolations()) {
            var field = x.getPropertyPath().toString();
            var msg = x.getMessage();
            messages.add("[%s]: %s".formatted(field, msg));
        }
        var response = new ErrorResponse(messages);

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ErrorResponse> proceessMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        var illegalArgument = findIllegalArgument(e);

        if (illegalArgument.isPresent()) {
            return processIllegalArgumentException(illegalArgument.get());
        }

        return processUnhandledException(new RuntimeException(e));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ErrorResponse> processIllegalArgumentException(IllegalArgumentException e) {
        var response = new ErrorResponse(e.getMessage());

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
    }

    private Optional<ConstraintViolationException> findConstraintViolation(RuntimeException e) {
        var attemptCount = 10;
        var cause = e.getCause();
        while (attemptCount-- > 0) {
            if (cause instanceof ConstraintViolationException) {
                return Optional.of((ConstraintViolationException) cause);
            } else if (cause.getCause() == null) {
                return Optional.empty();
            }
            cause = cause.getCause();
        }

        return Optional.empty();
    }

    private Optional<IllegalArgumentException> findIllegalArgument(MethodArgumentTypeMismatchException e) {
        var attemptCount = 10;
        var cause = e.getCause();
        while (attemptCount-- > 0) {
            if (cause instanceof IllegalArgumentException) {
                return Optional.of((IllegalArgumentException) cause);
            } else if (cause.getCause() == null) {
                return Optional.empty();
            }
            cause = cause.getCause();
        }

        return Optional.empty();
    }
}
