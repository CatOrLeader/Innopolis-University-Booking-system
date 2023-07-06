package Database.Controllers;

import Database.DbConnector;
import Database.Services.BookingService;
import Models.BookingModel;

import java.sql.Timestamp;
import java.util.ArrayList;

public class BookingController {
    // database connection
    private final DbConnector connection = new DbConnector();
    private final BookingService bookingService;

    public BookingController() {
        bookingService = new BookingService(connection.getConnection());
    }

    /**
     * Adds a new booking to the database
     *
     * @param bookingModel model of the booking
     */
    public void addOrUpdateBooking(BookingModel bookingModel) {
        if (bookingService.getBookingDataById(bookingModel.id) != null){
            bookingService.updateBooking(bookingModel);
        } else {
            bookingService.addNewBooking(bookingModel);
        }
    }

    /**
     * Returns data about the booking
     * by booking id
     *
     * @param bookingId id of the booking
     * @return model of the booking
     */
    public BookingModel getBookingDataById(String bookingId) {
        return bookingService.getBookingDataById(bookingId);
    }

    /**
     * Returns data about the bookings
     * by user's chat ID
     *
     * @param tgChatId ID of the user chat
     * @return list of user's bookings
     */
    public ArrayList<BookingModel> getBookingsByUserChatId(long tgChatId) {
        return bookingService.getBookingsByUserChatId(tgChatId);
    }

    /**
     * Filter for bookings by time
     *
     * @param start start date time (nullable)
     * @param end   end date time (nullable)
     * @return list of bookings
     */
    public ArrayList<BookingModel> getBookingsByTimePeriod(Timestamp start, Timestamp end) {
        return bookingService.getBookingsByTimePeriod(start, end);
    }

    /**
     * Confirms booking
     *
     * @param bookingId id of the booking
     */
    public void confirmBooking(String bookingId) {
        bookingService.confirmBooking(bookingId);
    }

    /**
     * Deletes booking
     *
     * @param bookingId id of the booking
     */
    public void deleteBooking(String bookingId) {
        bookingService.deleteBooking(bookingId);
    }
}
