package APIwrapper.requests;

import APIwrapper.json.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.awt.print.Book;
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

    // Incoming messages processor
    protected ArrayList<Room> getAllBookableRooms() {
        String content = formatStringToGson(rawRequest.getAllBookableRoomsUnformatted());

        // Catch any exception
        if (checkIfValidationError(content)) return null;

        // Parse to Java Class Objects
        Type roomArrayList = new TypeToken<ArrayList<Room>>(){}.getType();

        return gson.fromJson(content, roomArrayList);
    }

    // TODO: Check on real server (or mock)
    protected ArrayList<Room> getAllFreeRooms(GetFreeRoomRequest request) {
        // Forming json-type of the incoming request
        GetFreeRoomRequest request1 =
                new GetFreeRoomRequest(
                        "2023-06-19T15:46:45.207Z",
                        "2023-06-19T15:46:45.207Z"); // test request
        String jsonRequest = gson.toJson(request1);

        // Make a request and receive a response
        String content = formatStringToGson(rawRequest.getFreeRoomsUnformatted(jsonRequest));

        // Catch any exception
        if (checkIfValidationError(content)) return null;

        // Parse to Java Class Object
        Type roomArrayList = new TypeToken<ArrayList<Room>>(){}.getType();
        ArrayList<Room> rooms = gson.fromJson(content, roomArrayList);

        System.out.println(rooms);

        return rooms;
    }

    protected Booking bookRoom(String roomId, BookRoomRequest request) {
        // Forming json-type of the incoming request
        BookRoomRequest request1 =
                new BookRoomRequest(
                        "Booking #1",
                        "2023-06-19T15:46:45.207Z",
                        "2023-06-19T15:46:45.207Z",
                        "abrakadabra@innopolis.university"); // test request
        String jsonRequest = gson.toJson(request1);

        // Make a request and receive a response
        String content = formatStringToGson(rawRequest.bookRoomUnformatted(roomId, jsonRequest));

        // Catch any exceptions
        if (checkIfValidationError(content)) return null;
        if (checkIfBookRoomError(content)) return null;

        // Parse to Java Class Object
        Booking booking = gson.fromJson(content, Booking.class);

        System.out.println(booking);

        return booking;
    }

    protected ArrayList<Booking> queryBookings(QueryBookingsRequest request) {
        // Forming json-type of the incoming request
        QueryBookingsRequest request1 = new QueryBookingsRequest(
                        new BookingsFilter(
                                "Booking #1",
                                "2023-06-19T15:46:45.207Z",
                                new String[]{"3.2", "105"},
                                new String[]{"abrakadabra@innopolis.university"}
                        )
        ); // test request
        String jsonRequest = gson.toJson(request1);

        // Make a request and receive a response
        String content = formatStringToGson(rawRequest.queryBookingsUnformatted(jsonRequest));

        // Catch any exceptions
        if (checkIfValidationError(content)) return null;

        // Parse to Java Class Object
        Type bookingsArrayList = new TypeToken<ArrayList<Booking>>(){}.getType();
        ArrayList<Booking> bookings = gson.fromJson(content, bookingsArrayList);

        System.out.println(bookings);

        return bookings;
    }

    protected String deleteBooking(String bookingId) {
        // Make a request and receive a response
        String content = formatStringToGson(rawRequest.deleteBookingsUnformatted(bookingId));

        // Catch any exceptions
        if (checkIfValidationError(content)) return null;

        // Parse to Java Class Object
        String msg = gson.fromJson(content, String.class);

        System.out.println(msg);

        return msg;
    }

    // Additional Methods
    private String formatStringToGson(String content) {
        if (content == null) {
            return null;
        }

        String replacement = "\\" + "\"";

        return content.replaceAll("\"", replacement);
    }

    // Exception checkers
    private boolean checkIfValidationError(String content) {
        try {
            gson.fromJson(content, HTTPValidationError.class);
            System.out.println("HTTPValidation error");
            System.out.println(content);
            return true;
        } catch (Exception e) {
            System.out.println("Validation was successfully done");
            return false;
        }
    }

    private boolean checkIfBookRoomError(String content) {
        try {
            gson.fromJson(content, BookRoomError.class);
            System.out.println("This room cannot be booked for this user during this time period");
            System.out.println(content);
            return true;
        } catch (Exception e) {
            System.out.println("Bookings was successfully done");
            return false;
        }
    }

    public static void main(String[] args) {
        String url = "http://localhost:3000/";
        RequestFormatted requestFormatted =
                new RequestFormatted(url);

        requestFormatted.deleteBooking("idLol");
    }
}
