package APIWrapper.Requests;

import APIWrapper.Requests.APIResponses.ApiResponse;
import okhttp3.*;
import okhttp3.Request;

import java.io.IOException;

class RequestRaw {
    // Constants
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    // Initial
    private final OkHttpClient client;
    private final String url;

    protected RequestRaw(String url) {
        client = new OkHttpClient();
        this.url = url;
    }

    protected ApiResponse getAllBookableRoomsUnformatted() {
        String concreteUrl = url + "/rooms";

        Request request = new Request.Builder()
                .url(concreteUrl)
                .method("GET", null)
                .build();

        return execute(request);
    }

    protected ApiResponse getAllFreeRoomsUnformatted(String jsonRequestBody) {
        String concreteUrl = url + "/rooms/free";

        Request request = createPOST(concreteUrl, jsonRequestBody);

        return execute(request);
    }

    protected ApiResponse bookRoomUnformatted(String roomId, String jsonRequestBody) {
        String concreteUrl = url + "/rooms/" + roomId + "/book";

        Request request = createPOST(concreteUrl, jsonRequestBody);

        return execute(request);
    }

    protected ApiResponse queryBookingsUnformatted(String jsonRequestBody) {
        String concreteUrl = url + "/bookings/query";

        Request request = createPOST(concreteUrl, jsonRequestBody);

        return execute(request);
    }

    protected ApiResponse deleteBookingUnformatted(String bookingId) {
        String concreteUrl = url + "/bookings/" + bookingId;

        Request request = new Request.Builder()
                .url(concreteUrl)
                .method("DELETE", null)
                .addHeader("Accept", "application/json")
                .build();

        return execute(request);
    }

    // Additional methods
    private Request createPOST(String url, String jsonRequestBody) {
        return new Request.Builder()
                .url(url)
                .post(RequestBody.create(jsonRequestBody, JSON))
                .addHeader("Accept", "application/json")
                .build();
    }

    private ApiResponse execute(Request request) {
        try (Response response = client.newCall(request).execute()) {
            System.out.println();
            System.out.println("----------Last Response----------");
            System.out.println("Request method: " + request.method());
            System.out.println("Response code: " + response.code());
            System.out.println("Response message: " + response.message());
            System.out.println("---------------------------------");
            System.out.println();
            if (response.body() != null) {
                return new ApiResponse(response.code(), response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return null;
    }
}
