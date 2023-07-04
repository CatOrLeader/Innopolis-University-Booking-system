package Server.MockTestingForDevs;

import APIWrapper.Json.BookRoomRequest;
import APIWrapper.Json.Booking;
import APIWrapper.Json.BookingsFilter;
import APIWrapper.Json.QueryBookingsRequest;
import APIWrapper.Requests.Request;

import java.util.ArrayList;

public class RequestsExamples {
    public static void main(String[] args) {
        Request request = new Request("http://localhost:3000");

        BookRoomRequest bookRoomRequest = new BookRoomRequest(
                "Test bookings from the mockTesting... directory",
                "22.06.23 05:00",
                90,
                "email@innopolis.university"
        );

        QueryBookingsRequest queryBookingsRequest = new QueryBookingsRequest(
                new BookingsFilter(
                        "22.06.23 04:30",
                        "22.06.23 19:00",
                        new String[]{"3.5"},
                        new String[]{"email@innopolis.university"}
                )
        );

        ArrayList<Booking> bookings = request.getBookingsByUser("email@innopolis.university");
        System.out.println(bookings);
    }
}
