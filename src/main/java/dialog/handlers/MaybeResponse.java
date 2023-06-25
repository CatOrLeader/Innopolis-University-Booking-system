package dialog.handlers;

/**
 * Wrapper for Response.
 * To distinguish handled requests and unhandled.
 */
public class MaybeResponse {
    private Response innerResponse = null;

    /**
     * Constructor for handled request's response
     * @param response resulting response from handler
     */
    public MaybeResponse(Response response) {
        innerResponse = response;
    }

    /**
     * Constructor for unhandled request's response
     */
    public MaybeResponse() {

    }

    /**
     * Method to check whether response exists or not
     * @return true if response exists, false otherwise
     */
    public boolean hasResponse() {
        return innerResponse != null;
    }

    /**
     * Method to get response
     * @return response
     */
    public Response getResponse() {
        return innerResponse;
    }
}
