package APIWrapper.requests.APIResponses;

import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public class ApiResponse {
    // Initial
    private final int code;
    private final String body;

    public ApiResponse(int code, String body) {
        this.code = code;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }
}
