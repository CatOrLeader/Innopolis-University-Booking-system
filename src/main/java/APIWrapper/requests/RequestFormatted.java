package APIWrapper.requests;

import APIWrapper.json.*;
import APIWrapper.requests.APIResponses.ApiResponse;
import APIWrapper.requests.APIResponses.ApiResponses;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

class RequestFormatted {
    private final String url;
    private final RequestRaw rawRequest;
    private final Gson gson;

    public RequestFormatted(String url) {
        this.url = url;
        rawRequest = new RequestRaw(url);
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    // Incoming messages processors
    protected ArrayList<Room> getAllBookableRooms() {
        ApiResponse response = rawRequest.getAllBookableRoomsUnformatted();

        // Catch responses
        if (isNotOK(response)) return null;

        // Parse to Java Class Object
        Type roomArrayList = new TypeToken<ArrayList<Room>>() {}.getType();
        return gson.fromJson(response.getBody(), roomArrayList);
    }

    protected ArrayList<Room> getAllFreeRooms(GetFreeRoomsRequest request) {
        request.formatToSend();
        String jsonRequest = gson.toJson(request);

        // Make request
        ApiResponse response = rawRequest.getAllFreeRoomsUnformatted(jsonRequest);

        // Catch responses
        if (isNotOK(response)) return null;

        // Parse to Java Class Object
        Type roomArrayList = new TypeToken<ArrayList<Room>>() {
        }.getType();
        return gson.fromJson(response.getBody(), roomArrayList);
    }

    protected Booking bookRoom(String roomId, BookRoomRequest request) {
        request.formatToSend();
        String jsonRequest = gson.toJson(request);

        // Make a request and receive a response
        ApiResponse response = rawRequest.bookRoomUnformatted(roomId, jsonRequest);

        // Catch responses
        if (isNotOK(response)) return null;

        // Parse to Java Class Object
        return gson.fromJson(response.getBody(), Booking.class);
    }

    protected ArrayList<Booking> queryBookings(QueryBookingsRequest request) {
        request.formatToSend();
        String jsonRequest = gson.toJson(request);

        // Make a request and receive a response
        ApiResponse response = rawRequest.queryBookingsUnformatted(jsonRequest);

        // Catch responses
        if (isNotOK(response)) return null;

        // Parse to Java Class Object
        Type bookingsArrayList = new TypeToken<ArrayList<Booking>>() {
        }.getType();
        return gson.fromJson(response.getBody(), bookingsArrayList);
    }

    protected String deleteBooking(String bookingId) {
        // Make a request and receive a response
        ApiResponse response = rawRequest.deleteBookingUnformatted(bookingId);

        // Catch responses
        if (isNotOK(response)) return null;

        return response.getBody();
    }

    // Catchers
    private boolean isNotOK(ApiResponse response) {
        if (!(response.getCode() == ApiResponses.OK.code)) {
            System.out.println(response.getCode() + "\n" + response.getBody());
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        RequestFormatted requestFormatted = new RequestFormatted("http://localhost:3000");

        GetFreeRoomsRequest request = new GetFreeRoomsRequest(
                "04.07.23 04:00", 90
        );

        BookRoomRequest request1 = new BookRoomRequest(
                "title", "04.07.23 04:00", 90, "a.mukhutdinov@innopolis.university"
        );

        QueryBookingsRequest request2 = new QueryBookingsRequest(
                new BookingsFilter(
                        null, null,
                        new String[]{}, new String[]{}
                )
        );

        requestFormatted.getAllBookableRooms();
    }
}
