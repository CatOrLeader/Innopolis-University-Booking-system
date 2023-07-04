package APIWrapper.Json;

public class HTTPValidationError {
    public final ValidationError detail;

    public HTTPValidationError(ValidationError wrongHttpRequest) {
        this.detail = wrongHttpRequest;
    }
}
