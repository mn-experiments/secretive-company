package secretive.exception;

import java.time.OffsetDateTime;
import java.util.List;

public record ErrorResponse(
        List<String> errors,
        OffsetDateTime dateTime
) {
    public ErrorResponse(String error) {
        this(List.of(error), OffsetDateTime.now());
    }

    public ErrorResponse(List<String> error) {
        this(error, OffsetDateTime.now());
    }
}
