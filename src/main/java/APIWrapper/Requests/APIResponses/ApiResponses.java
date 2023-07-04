package APIWrapper.Requests.APIResponses;

public enum ApiResponses {
    OK(200), BAD_REQUEST(400), UNAUTHORIZED(401), NOT_FOUND(404), UNPROCESSABLE_ENTITY(422);

    // Initial
    public final int code;

    ApiResponses(int code) {
        this.code = code;
    }
}
