package APIWrapper.Requests.APIErrorResponses;

public class HTTPValidationError {
    public final ValidationError detail;

    public HTTPValidationError(ValidationError wrongHttpRequest) {
        this.detail = wrongHttpRequest;
    }
}
