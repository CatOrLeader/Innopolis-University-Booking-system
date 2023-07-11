package Database.Controllers;

import Database.DbConnector;
import Database.Services.BookingService;
import Models.Booking;
import Utilities.Config;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
     * @param booking model of the booking
     */
    public void addOrUpdateBooking(Booking booking) {
        initializeConfirmStatus(booking);
        if (bookingService.getBookingDataById(booking.id) != null){
            bookingService.updateBooking(booking);
        } else {
            bookingService.addNewBooking(booking);
        }
    }

    /**
     * Returns data about the booking
     * by booking id
     *
     * @param bookingId id of the booking
     * @return model of the booking
     */
    public Booking getBookingById(String bookingId) {
        return bookingService.getBookingDataById(bookingId);
    }

    /**
     * Returns data about the bookings
     * by user's chat ID
     *
     * @param tgChatId ID of the user chat
     * @return list of user's bookings
     */
    public ArrayList<Booking> getBookingsByChatId(long tgChatId) {
        return bookingService.getBookingsByUserChatId(tgChatId);
    }

    /**
     * Filter for bookings by time period
     *
     * @param start start date time (nullable)
     * @param end   end date time (nullable)
     * @return list of bookings
     */
    public ArrayList<Booking> getBookingsByTimePeriod(LocalDateTime start, LocalDateTime end) {
        return bookingService.getBookingsByTimePeriod(
                start == null ? null : Timestamp.valueOf(start),
                end == null ? null : Timestamp.valueOf(end)
        );
    }

    /**
     * Filter for bookings by start time
     *
     * @param time start date time
     * @return list of bookings
     */
    public List<Booking> getBookingsByTimeStart(LocalDateTime time) {
        time = time.withNano(0).withSecond(0);
        return bookingService.
                getBookingsByTimeStart(Timestamp.valueOf(time)).
                stream().toList();
    }

    public List<Booking> getBookingsNow() {
        return getBookingsByTimeStart(LocalDateTime.now());
    }

    public List<Booking> getBookingsToNotify() {
        return getBookingsByTimeStart(LocalDateTime.now().plusMinutes(Config.bookingReminderPeriod()));
    }

    /**
     * Confirms booking
     *
     * @param bookingId id of the booking
     */
    public void setConfirmed(String bookingId) {
        bookingService.confirmBooking(bookingId);
    }

    /**
     * Deletes booking
     *
     * @param bookingId id of the booking
     */
    public void removeBooking(String bookingId) {
        bookingService.deleteBooking(bookingId);
    }

    private void initializeConfirmStatus(Booking booking) {
        var bookTime = ZonedDateTime.of(
                LocalDateTime.ofInstant(
                        Instant.from(DateTimeFormatter.ISO_INSTANT.parse(booking.start)),
                        ZoneOffset.UTC),
                ZoneOffset.UTC
        );
        var curTime = ZonedDateTime.of(
                LocalDateTime.now(),
                ZoneOffset.UTC
        );
        booking.isConfirmed = (
                ChronoUnit.MINUTES.between(curTime, bookTime) <= Config.bookingSafePeriod());
    }
}
