package secretive.exception;

public enum ErrorMessage {
    NULL_NOT_ALLOWED("the field [%s] should not be null");

    private final String template;

    public String getTemplate() {
        return template;
    }

    ErrorMessage(String template) {
        this.template = template;
    }
}
