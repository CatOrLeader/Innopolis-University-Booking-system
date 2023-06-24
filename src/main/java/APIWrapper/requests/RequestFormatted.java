package APIWrapper.requests;

import APIWrapper.json.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
        String content = formatStringToGson(rawRequest.getAllBookableRoomsUnformatted());

        // Catch any exception
        if (checkIfValidationError(content)) return null;

        // Parse to Java Class Objects
        Type roomArrayList = new TypeToken<ArrayList<Room>>() {
        }.getType();

        try {
            updateJson(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gson.fromJson(content, roomArrayList);
    }

    protected ArrayList<Room> getAllFreeRooms(GetFreeRoomsRequest request) {
        request.formatToSend();
        String jsonRequest = gson.toJson(request);

        // Make a request and receive a response
        String content = formatStringToGson(rawRequest.getFreeRoomsUnformatted(jsonRequest));

        // Catch any exception
        if (checkIfValidationError(content)) return null;

        // Parse to Java Class Object
        Type roomArrayList = new TypeToken<ArrayList<Room>>() {
        }.getType();
        ArrayList<Room> rooms = gson.fromJson(content, roomArrayList);

        try {
            updateJson(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    protected Booking bookRoom(String roomId, BookRoomRequest request) {
        request.formatToSend();
        String jsonRequest = gson.toJson(request);

        // Make a request and receive a response
        String content = formatStringToGson(rawRequest.bookRoomUnformatted(roomId, jsonRequest));

        // Catch any exceptions
        if (checkIfValidationError(content)) return null;
        if (checkIfBookRoomError(content)) return null;

        // Parse to Java Class Object
        Booking booking = gson.fromJson(content, Booking.class);

        try {
            updateJson(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return booking;
    }

    protected ArrayList<Booking> queryBookings(QueryBookingsRequest request) {
        request.formatToSend();
        String jsonRequest = gson.toJson(request);

        // Make a request and receive a response
        String content = formatStringToGson(rawRequest.queryBookingsUnformatted(jsonRequest));

        // Catch any exceptions
        if (checkIfValidationError(content)) return null;

        // Parse to Java Class Object
        Type bookingsArrayList = new TypeToken<ArrayList<Booking>>() {
        }.getType();
        ArrayList<Booking> bookings = gson.fromJson(content, bookingsArrayList);

        try {
            updateJson(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bookings;
    }

    protected String deleteBooking(String bookingId) {
        // Make a request and receive a response
        String content = formatStringToGson(rawRequest.deleteBookingsUnformatted(bookingId));

        // Catch any exceptions
        if (checkIfValidationError(content)) return null;

        // Parse to Java Class Object
        System.out.println(content);
        String msg = gson.fromJson(content, String.class);

        try {
            updateJson(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    private void updateJson(String body) throws IOException {
        if (body == null) return;

        BufferedWriter writer = new BufferedWriter(
                new FileWriter("src/main/java/mockTestingForDevs/serverResponse.json"));
        writer.write(body);
        writer.close();
    }

    // Exception checkers
    private boolean checkIfValidationError(String content) {
        try {
            HTTPValidationError error = gson.fromJson(content, HTTPValidationError.class);
            updateJson(content);
            System.out.println("HTTPValidation error" +
                    "\nLoc: " + error.detail.loc +
                    "\nMsg: " + error.detail.msg +
                    "\nType: " + error.detail.type);
            return true;
        } catch (Exception e) {
            System.out.println("Validation was successfully done");
            return false;
        }
    }

    private boolean checkIfBookRoomError(String content) {
        try {
            BookRoomError error = gson.fromJson(content, BookRoomError.class);
            updateJson(content);

            // Check if the incoming body satisfy to the BookRoomError Class
            if (error.message == null) throw new Exception();

            System.out.println("This room cannot be booked for this user during this time period");
            System.out.println("BookRoomError message: " + error.message);
            return true;
        } catch (Exception e) {
            System.out.println("Booking was successfully done");
            return false;
        }
    }
}
