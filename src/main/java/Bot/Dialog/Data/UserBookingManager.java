package Bot.Dialog.Data;

import Database.Controllers.BookingController;
import Models.BookingModel;
import Utilities.Config;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class UserBookingManager {
    /**
     * THIS IS USED AS A DATABASE
     */
    private final BookingController bookingData = new BookingController();

    public UserBooking getBookingById(String id) {
        var booking = bookingData.getBookingDataById(id);
        return booking == null ? null : booking.toUserBooking();
    }

    public List<UserBooking> getBookingsNow() {
        return getBookingsAt(LocalDateTime.now());
    }

    public List<UserBooking> getBookingsToNotify() {
        return getBookingsAt(LocalDateTime.now().plusMinutes(Config.bookingReminderPeriod()));
    }

    public void addBooking(UserBooking booking) {
        initializeConfirmStatus(booking);
        bookingData.addOrUpdateBooking(booking.toBookingModel());
    }

    public void removeBookingById(String id) {
        bookingData.deleteBooking(id);
    }

    public void setConfirmed(String id) {
        bookingData.confirmBooking(id);
    }

    private List<UserBooking> getBookingsAt(LocalDateTime time) {
        time = time.withNano(0).withSecond(0);
        return bookingData.
                getBookingsByTimeStart(Timestamp.valueOf(time)).
                stream().
                map(BookingModel::toUserBooking).toList();
    }

    private void initializeConfirmStatus(UserBooking booking) {
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
