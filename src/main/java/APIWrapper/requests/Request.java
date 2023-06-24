package APIWrapper.requests;

import APIWrapper.json.*;

import java.util.ArrayList;

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

    // TODO: We will have a database, which connects all users' emails with their tgID
    // so it will be convenient to get all the bookings by user ID.
    // Also, do not forget about statically receive all the rooms from the Outlook and never do it again :9
    public ArrayList<Booking> getBookingsByUser(String email) {
        ArrayList<Room> rooms = getAllBookableRooms();
        String[] ids = extractRoomIds(rooms);


        BookingsFilter bookingsFilter = new BookingsFilter(
                "23.06.23 12:15",
                "23.06.23 13:15",
                ids,
                new String[]{email}
        );

        return queryBookings(new QueryBookingsRequest(bookingsFilter));
    }

    private String[] extractRoomIds(ArrayList<Room> rooms) {
        ArrayList<String> ids = new ArrayList<>();
        rooms.forEach(room -> ids.add(room.id));
        String[] output = new String[ids.size()];

        for (int i = 0; i < ids.size(); i++) {
            output[i] = ids.get(i);
        }

        return output;
    }
}
