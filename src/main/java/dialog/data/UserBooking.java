package dialog.data;

import APIWrapper.json.Booking;

public class UserBooking {
    private final long userId;
    private final Booking booking;
    private boolean isConfirmed;

    public UserBooking(long userId, Booking booking, boolean isConfirmed) {
        this.userId = userId;
        this.booking = booking;
        this.isConfirmed = isConfirmed;
    }

    public long getUserId() {
        return userId;
    }


    public Booking getBooking() {
        return booking;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }
}
