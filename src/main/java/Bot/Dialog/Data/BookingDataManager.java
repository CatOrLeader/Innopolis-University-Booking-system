package Bot.Dialog.Data;

import APIWrapper.Json.Booking;
import APIWrapper.Utilities.DateTime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// TODO: DO NOT FORGET ABOUT LOCAL TIME ON DIFFERENT MACHINE!
public class BookingDataManager {
    // TODO: get rid of this, this is instead of database
    private static final Map<String, Booking> bookingById = new HashMap<>();
    private static final Map<String, List<UserBooking>> bookingData =
            new HashMap<>();

    public List<UserBooking> getBookingsNow() {
        return getBookingsAtTime(LocalDateTime.now());
    }

    public List<UserBooking> getBookingsToConfirm() {
        return getBookingsAtTime(LocalDateTime.now().plusMinutes(16));
    }

    private List<UserBooking> getBookingsAtTime(LocalDateTime time) {
        var fTime = formatTime(time);
        if (!bookingData.containsKey(fTime)) {
            return new LinkedList<>();
        }
        return bookingData.get(fTime);
    }

    private String formatTime(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"));
    }

    public void addBooking(Booking booking, long userId) {
        var bookingTime = DateTime.formatToConvenient(booking.start);
        bookingById.put(booking.id, booking);
        bookingData.putIfAbsent(bookingTime, new LinkedList<>());
        var dt = ZonedDateTime.parse(booking.start, DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault()));
        var isConfirmed = (ChronoUnit.MINUTES.between(dt, ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault())) <= 15);
        bookingData.get(bookingTime).add(new UserBooking(userId, booking, isConfirmed));
    }

    public void removeBooking(Booking booking) {
        bookingById.remove(booking.id);
        var list = bookingData.get(DateTime.formatToConvenient(booking.start));
        if (list == null) {
            return;
        }
        list.removeIf(confirmation -> confirmation.getBooking().equals(booking));
    }

    // FIXME: not optimal, will be better with database
    public void setConfirmed(Booking booking) {
        var time = DateTime.formatToConvenient(booking.start);
        bookingData.get(time).forEach(userBooking -> {
            if (userBooking.getBooking().equals(booking)) {
                userBooking.setConfirmed(true);
            }
        });
    }

    public Booking getBookingById(String id) {
        return bookingById.get(id);
    }
}
