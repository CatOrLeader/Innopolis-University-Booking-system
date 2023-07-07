package Bot.Dialog.Data;

import Utilities.Config;
import Utilities.DateTime;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserBookingManager {
    /**
     * THIS IS USED AS A DATABASE
     */
    private static final Map<String, UserBooking> bookingById = new HashMap<>();
    private static final Map<String, List<UserBooking>> bookingsByTime = new HashMap<>();

    public UserBooking getBookingById(String id) {
        return bookingById.get(id);
    }

    public List<UserBooking> getBookingsNow() {
        return getBookingsAt(formatTime(LocalDateTime.now()));
    }

    public List<UserBooking> getBookingsToNotify() {
        return getBookingsAt(formatTime(LocalDateTime.now().plusMinutes(Config.bookingReminderPeriod())));
    }

    public void addBooking(UserBooking booking) {
        bookingById.put(booking.id, booking);
        var fTime = DateTime.formatToConvenient(booking.start);
        initializeConfirmStatus(booking);
        bookingsByTime.putIfAbsent(fTime, new LinkedList<>());
        bookingsByTime.get(fTime).add(booking);
    }

    public void removeBookingById(String id) {
        try {
            var booking = getBookingById(id);
            getBookingsAt(DateTime.formatToConvenient(booking.start)).remove(booking);
            bookingById.remove(id);
        } catch (Exception ignored) {
            // to ignore current async between our db and server
        }
    }

    public void setConfirmed(String id) {
        if (getBookingById(id) != null) {
            getBookingById(id).isConfirmed = true;
        }
    }

    private List<UserBooking> getBookingsAt(String time) {
        if (!bookingsByTime.containsKey(time)) {
            return new LinkedList<>();
        }
        return bookingsByTime.get(time);
    }

    private String formatTime(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"));
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
