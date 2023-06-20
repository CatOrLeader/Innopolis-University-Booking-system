package APIWrapper.json;

public class HTTPValidationError {
    public ValidationError detail;

    public HTTPValidationError(ValidationError wrongHttpRequest) {
        this.detail = wrongHttpRequest;
    }
}
