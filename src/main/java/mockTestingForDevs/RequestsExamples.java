package mockTestingForDevs;

import APIWrapper.json.BookRoomRequest;
import APIWrapper.json.BookingsFilter;
import APIWrapper.json.GetFreeRoomsRequest;
import APIWrapper.json.QueryBookingsRequest;
import APIWrapper.requests.Request;
import APIWrapper.json.*;

import java.util.ArrayList;

public class RequestsExamples {
    public static void main(String[] args) {
        Request request = new Request("http://localhost:3000");

        GetFreeRoomsRequest getFreeRoomsRequest = new GetFreeRoomsRequest(
                "not implemented yet",
                "not implemented yet"
        );

        BookRoomRequest bookRoomRequest = new BookRoomRequest(
                "Test bookings from the mockTesting... directory",
                "not implemented yet",
                "not implemented yet",
                "email@innopolis.university"
        );

        QueryBookingsRequest queryBookingsRequest = new QueryBookingsRequest(
                new BookingsFilter(
                        "not implemented yet",
                        "not implemented yet",
                        new String[]{"3.1"},
                        new String[]{"email@innopolis.university"}
                )
        );

//        ArrayList<Room> rooms = request.getAllBookableRooms();
//        ArrayList<Room> freeRooms = request.getAllFreeRooms(getFreeRoomsRequest);
//        Booking booking = request.bookRoom("3.1", bookRoomRequest);
//        ArrayList<Booking> bookings = request.queryBookings(queryBookingsRequest);
//        String deleteReport = request.deleteBooking("1");
    }
}
