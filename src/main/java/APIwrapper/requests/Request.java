package APIwrapper.requests;

import APIwrapper.json.*;

import java.util.ArrayList;

/*
    TODO:
    A) Remember to change the links when work with the realApi (enum Paths in RequestRaw)
 */

public class Request {
    private String url;
    private final RequestFormatted formatter;

    public Request(String url) {
        this.url = url;
        formatter = new RequestFormatted(url);
    }

    // Endpoints
    public ArrayList<Room> getAllBookableRooms() {
        return formatter.getAllBookableRooms();
    }

    public ArrayList<Room> getAllFreeRooms(GetFreeRoomRequest request) {
        return formatter.getAllFreeRooms(request);
    }

    public Booking bookRoom(String roomId, BookRoomRequest request) {
        return formatter.bookRoom(roomId, request);
    }

    public ArrayList<Booking> queryBookings(QueryBookingsRequest request) {
        return formatter.queryBookings(request);
    }

    public String deleteBooking(String bookingId) {
        return formatter.deleteBooking(bookingId);
    }
}
