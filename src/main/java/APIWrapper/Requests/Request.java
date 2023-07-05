package APIWrapper.Requests;

import APIWrapper.Json.*;

import java.util.ArrayList;

public class Request {
    private final RequestFormatted formatter;
    private final String url;

    public Request() {
        this.url = System.getenv("MOCK_SERVER_URL");
        formatter = new RequestFormatted(url);
    }

    // Endpoints
    public ArrayList<Room> getAllBookableRooms() {
        return formatter.getAllBookableRooms();
    }

    public ArrayList<Room> getAllFreeRooms(GetFreeRoomsRequest request) {
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

    // Additional methods
    public ArrayList<Booking> getBookingsByUser(String email) {
        BookingsFilter bookingsFilter = new BookingsFilter(
                null,
                null,
                new String[]{},
                new String[]{email}
        );

        return queryBookings(new QueryBookingsRequest(bookingsFilter));
    }
}
