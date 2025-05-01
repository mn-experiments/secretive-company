package secretive.exception;

public enum ErrorMessage {
    NOT_FOUND_BY_NAME("object [%s] with name [%s] does not exist"),
    NOT_FOUND_BY_ID("object [%s] with id [%s] does not exist");

    private final String template;

    public String getTemplate() {
        return template;
    }

    ErrorMessage(String template) {
        this.template = template;
    }
}
