package secretive.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import secretive.exception.throwable.ApiException;

import java.util.ArrayList;

@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    void processUnhandledException(RuntimeException e) {
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
}
