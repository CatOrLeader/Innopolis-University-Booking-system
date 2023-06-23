package APIWrapper.json;

import APIWrapper.utilities.DateTime;

public class Booking {
    // Exposed fields
    public String id;
    public String title;
    public String start;
    public String end;
    public Room room;
    public String owner_email;

    // Hidden fields
    public transient int duration;

    public Booking(String id, String title, String start, String end, Room room, String owner_email) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.room = room;
        this.owner_email = owner_email;
    }

    public Booking(String id, String title, String start, int duration, Room room, String owner_email) {
        this.id = id;
        this.title = title;
        this.room = room;
        this.owner_email = owner_email;
        this.start = start;
        this.duration = duration;
    }

    public Booking() {

    }

    // Class constructor
    public BookRoomRequest convertToBookRoomRequest() {
        BookRoomRequest request = new BookRoomRequest(
                title,
                start,
                duration,
                owner_email
        );

        // It is necessary to make actions in this order
        parseDateTimeToOutput();

        return request;
    }

    // Additional methods
    private void parseDateTimeToOutput() {
        DateTime dateTime = new DateTime(start, duration);

        start = dateTime.getOutputStart();
        end = dateTime.getOutputEnd();
    }
}
