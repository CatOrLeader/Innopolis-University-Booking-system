package Models;

import java.sql.Timestamp;

/**
 * Booking representation in the database
 */
public class BookingModel {
    public final String id;
    public long tgChatId;
    public String title;
    public String roomId;
    public Timestamp start;
    public Timestamp end;
    public boolean isConfirmed;

    public BookingModel(String id, long tgChatId, String title, String roomId, Timestamp start, Timestamp end, boolean isConfirmed) {
        this.id = id;
        this.tgChatId = tgChatId;
        this.title = title;
        this.roomId = roomId;
        this.start = start;
        this.end = end;
        this.isConfirmed = isConfirmed;
    }
}
