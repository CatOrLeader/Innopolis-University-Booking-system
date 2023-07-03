package dialog.data;

import APIWrapper.json.Booking;
import APIWrapper.utilities.DateTime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// TODO: DO NOT FORGET ABOUT LOCAL TIME ON DIFFERENT MACHINE!
public class BookingDataManager {
    // TODO: get rid of this
    private static final Map<String, Booking> bookingById = new HashMap<>();
    private static final Map<String, List<BookingReminder>> bookingData =
            new HashMap<>();

    public List<BookingReminder> getBookingsNow() {
        return getBookingsAtTime(LocalDateTime.now());
    }

    public List<BookingReminder> getBookingsToConfirm() {
        return getBookingsAtTime(LocalDateTime.now().plusMinutes(15));
    }

    private List<BookingReminder> getBookingsAtTime(LocalDateTime time) {
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
        // TODO: determine whether booking really confirmed
        var bookingTime = DateTime.formatToConvenient(booking.start);
        bookingById.put(booking.id, booking);
        bookingData.putIfAbsent(bookingTime, new LinkedList<>());
        bookingData.get(bookingTime).add(new BookingReminder(userId, booking, false));
    }

    public void removeBooking(Booking booking) {
        bookingById.remove(booking.id);
        var list = bookingData.get(DateTime.formatToConvenient(booking.start));
        list.removeIf(confirmation -> confirmation.getBooking().equals(booking));
    }

    // FIXME: not optimal
    public void setConfirmed(Booking booking) {
        var time = DateTime.formatToConvenient(booking.start);
        bookingData.get(time).forEach(bookingReminder -> {
            if (bookingReminder.getBooking().equals(booking)) {
                bookingReminder.setConfirmed(true);
            }
        });
    }

    public Booking getBookingById(String id) {
        return bookingById.get(id);
    }
}
