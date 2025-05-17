package secretive.exception;

public enum ErrorMessage {
    NOT_FOUND_BY_NAME("object [%s] with name [%s] does not exist"),
    NOT_FOUND_BY_ID("object [%s] with id [%s] does not exist"),
    RELATION_NOT_FOUND_BY_ID("a link to object [%s] with id [%s] is not possible because it does not exist"),
    OBJECT_FIELD_SHOULD_NOT_BE_NULL("[%s.%s] should not be null");

    private final String template;

    public String getTemplate() {
        return template;
    }

    ErrorMessage(String template) {
        this.template = template;
    }
}
