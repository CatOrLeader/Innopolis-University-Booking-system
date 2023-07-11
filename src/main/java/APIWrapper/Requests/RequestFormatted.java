package APIWrapper.Requests;

import APIWrapper.Requests.APIResponses.ApiResponse;
import APIWrapper.Requests.APIResponses.ApiResponses;
import Models.*;
import Utilities.Services;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

class RequestFormatted {
    private final String url;
    private final RequestRaw rawRequest;

    public RequestFormatted(String url) {
        this.url = url;
        rawRequest = new RequestRaw(url);
    }

    // Incoming messages processors
    protected ArrayList<Room> getAllBookableRooms() {
        ApiResponse response = rawRequest.getAllBookableRoomsUnformatted();

        // Catch responses
        if (isNotOK(response)) return null;

        // Parse to Java Class Object
        Type roomArrayList = new TypeToken<ArrayList<Room>>() {}.getType();
        return Services.gson.fromJson(response.body(), roomArrayList);
    }

    protected ArrayList<Room> getAllFreeRooms(GetFreeRoomsRequest request) {
        request.formatToSend();
        String jsonRequest = Services.gson.toJson(request);

        // Make request
        ApiResponse response = rawRequest.getAllFreeRoomsUnformatted(jsonRequest);

        // Catch responses
        if (isNotOK(response)) return null;

        // Parse to Java Class Object
        Type roomArrayList = new TypeToken<ArrayList<Room>>() {
        }.getType();
        return Services.gson.fromJson(response.body(), roomArrayList);
    }

    protected Booking bookRoom(String roomId, BookRoomRequest request) {
        request.formatToSend();
        String jsonRequest = Services.gson.toJson(request);

        // Make a request and receive a response
        ApiResponse response = rawRequest.bookRoomUnformatted(roomId, jsonRequest);

        // Catch responses
        if (isNotOK(response)) return null;

        // Parse to Java Class Object
        return Services.gson.fromJson(response.body(), Booking.class);
    }

    protected ArrayList<Booking> queryBookings(QueryBookingsRequest request) {
        request.formatToSend();
        String jsonRequest = Services.gson.toJson(request);

        // Make a request and receive a response
        ApiResponse response = rawRequest.queryBookingsUnformatted(jsonRequest);

        // Catch responses
        if (isNotOK(response)) return null;

        // Parse to Java Class Object
        Type bookingsArrayList = new TypeToken<ArrayList<Booking>>() {
        }.getType();
        return Services.gson.fromJson(response.body(), bookingsArrayList);
    }

    protected String deleteBooking(String bookingId) {
        // Make a request and receive a response
        ApiResponse response = rawRequest.deleteBookingUnformatted(bookingId);

        // Catch responses
        if (isNotOK(response)) return null;

        return response.body();
    }

    // Catchers
    private boolean isNotOK(ApiResponse response) {
        if (!(response.code() == ApiResponses.OK.code)) {
            System.out.println(response.code() + "\n" + response.body());
            return true;
        }

        return false;
    }
}
