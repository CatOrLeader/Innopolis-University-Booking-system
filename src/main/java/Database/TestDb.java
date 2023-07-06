package Database;

import Database.Controllers.BookingController;
import Models.BookingModel;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TestDb {
    public static void main(String[] args) {
        LocalDateTime start = LocalDateTime.of(2023, 7, 6, 14, 30, 0);
        LocalDateTime end = LocalDateTime.of(2023, 7, 6, 16, 30, 0);

        BookingModel bookingModel = new BookingModel("sdfdsf", 759652782, "booking 1", "100", Timestamp.valueOf(start), Timestamp.valueOf(end), false);

        BookingController bookingController = new BookingController();

        bookingController.addOrUpdateBooking(bookingModel);
        System.out.println(bookingController.getBookingDataById("sdfdsf"));
        System.out.println(bookingController.getBookingsByUserChatId(759652782));
        bookingController.confirmBooking("sdfdsf");
        BookingModel bookingModelUpd = new BookingModel("sdfdsf", 759652782, "booking 1 upd", "100", Timestamp.valueOf(start), Timestamp.valueOf(end), false);
        bookingController.addOrUpdateBooking(bookingModelUpd);
        System.out.println(bookingController.getBookingsByTimePeriod(Timestamp.valueOf(start), Timestamp.valueOf(end)));
        System.out.println(bookingController.getBookingsByTimePeriod(Timestamp.valueOf(start), null));
        System.out.println(bookingController.getBookingsByTimePeriod(null, Timestamp.valueOf(end)));
        bookingController.deleteBooking("sdfdsf");
    }
}
