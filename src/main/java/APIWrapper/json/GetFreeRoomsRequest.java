package APIWrapper.json;

import APIWrapper.utilities.DateTime;

public class GetFreeRoomsRequest {
    private final transient boolean isDurationUsed;
    // Exposed Fields
    public String start;
    public String end;
    // Hidden fields
    public transient int duration;

    public GetFreeRoomsRequest(String start, String end) {
        this.start = start;
        this.end = end;


        isDurationUsed = false;
    }

    public GetFreeRoomsRequest(String start, int duration) {
        this.start = start;
        this.duration = duration;

        isDurationUsed = true;
    }

    // Class constructor
    public void formatToSend() {
        parseDateTimeToOutput();
    }

    // Additional methods
    private void parseDateTimeToOutput() {
        DateTime dateTime;
        if (isDurationUsed) {
            dateTime = new DateTime(start, duration);
        } else {
            dateTime = new DateTime(start, end);
        }

        start = dateTime.getOutputStart();
        end = dateTime.getOutputEnd();
    }
}


