package Bot.Dialog.Data;

import Models.Booking;
import Models.BookingModel;
import Models.Room;

import static Utilities.DateTime.parseISOToTimestamp;

public class UserBooking extends Booking {
    public long userId;
    public boolean isConfirmed;

    public UserBooking(String id,
                       String title,
                       String start,
                       String end,
                       Room room,
                       String owner_email,
                       long userId,
                       boolean isConfirmed) {
        super(id, title, start, end, room, owner_email);
        this.userId = userId;
        this.isConfirmed = isConfirmed;
    }

    public UserBooking(Booking booking, long userId, boolean isConfirmed) {
        super(booking.id, booking.title, booking.start,
                booking.end, booking.room, booking.owner_email);
        this.userId = userId;
        this.isConfirmed = isConfirmed;
    }

    public BookingModel toBookingModel() {
        return new BookingModel(
                id,
                userId,
                owner_email,
                title,
                room.id,
                parseISOToTimestamp(start),
                parseISOToTimestamp(end),
                isConfirmed);
    }
}
