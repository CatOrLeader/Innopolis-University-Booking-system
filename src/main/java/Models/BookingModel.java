package Models;

import Bot.Dialog.Data.UserBooking;
import Database.Controllers.RoomController;
import Utilities.DateTime;

import java.sql.Timestamp;

/**
 * Booking representation in the database
 */
public class BookingModel {
    private final static RoomController roomData = new RoomController();
    public final String id;
    public long tgChatId;
    public String userEmail;
    public String title;
    public String roomId;
    public Timestamp start;
    public Timestamp end;
    public boolean isConfirmed;

    public BookingModel(String id, long tgChatId, String userEmail, String title, String roomId, Timestamp start, Timestamp end, boolean isConfirmed) {
        this.id = id;
        this.tgChatId = tgChatId;
        this.userEmail = userEmail;
        this.title = title;
        this.roomId = roomId;
        this.start = start;
        this.end = end;
        this.isConfirmed = isConfirmed;
    }

    public BookingModel(String id, long tgChatId, String title, String roomId, Timestamp start, Timestamp end, boolean isConfirmed) {
        this.id = id;
        this.tgChatId = tgChatId;
        this.title = title;
        this.roomId = roomId;
        this.start = start;
        this.end = end;
        this.isConfirmed = isConfirmed;
    }

    public UserBooking toUserBooking() {
        return new UserBooking(
                id,
                title,
                DateTime.formatTimestampToISO(start),
                DateTime.formatTimestampToISO(end),
                roomData.getRoomData(roomId),
                userEmail,
                tgChatId,
                isConfirmed);
    }
}
