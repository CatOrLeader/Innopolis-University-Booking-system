package io.swagger.database;

public class BookRoomException extends Throwable {
    @Override
    public String getMessage() {
        return "Booking is unavailable for now!";
    }
}
