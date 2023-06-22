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

//        GetFreeRoomsRequest getFreeRoomsRequest = new GetFreeRoomsRequest(
//                "22.06.23 04:30",
//                "22.06.23 19:30"
//        );

//        BookRoomRequest bookRoomRequest = new BookRoomRequest(
//                "Test bookings from the mockTesting... directory",
//                "22.06.23 05:00",
//                90,
//                "email@innopolis.university"
//        );

        QueryBookingsRequest queryBookingsRequest = new QueryBookingsRequest(
                new BookingsFilter(
                        "22.06.23 04:30",
                        "22.06.23 19:00",
                        new String[]{"3.5"},
                        new String[]{"email@innopolis.university"}
                )
        );

//        ArrayList<Room> rooms = request.getAllBookableRooms();
//        ArrayList<Room> freeRooms = request.getAllFreeRooms(getFreeRoomsRequest);
//        Booking booking = request.bookRoom("3.5", bookRoomRequest);
//        ArrayList<Booking> bookings = request.queryBookings(queryBookingsRequest);
//        String deleteReport = request.deleteBooking("2");
//        ArrayList<Booking> bookings = request.getBookingsByUser("email@innopolis.university");
    }
}
