package APIWrapper.Json;

import APIWrapper.Utilities.DateTime;

public class BookRoomRequest {
    // Exposed fields
    public String title;
    public String start;
    public String end;
    public String owner_email;

    // Hidden fields
    public transient int duration;

    public BookRoomRequest(String title, String start, int duration, String owner_email) {
        this.title = title;
        this.start = start;
        this.duration = duration;
        this.owner_email = owner_email;
    }

    public BookRoomRequest() {
    }

    // Class constructor
    public void formatToSend() {
        parseDateTimeToOutput();
    }

    // Additional methods
    private void parseDateTimeToOutput() {
        DateTime dateTime = new DateTime(start, duration);

        start = dateTime.getOutputStart();
        end = dateTime.getOutputEnd();
    }
}
