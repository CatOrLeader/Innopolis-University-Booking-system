package APIWrapper.json;

import APIWrapper.utilities.DateTime;

public class GetFreeRoomsRequest {
    public String start;
    public String end;

    public GetFreeRoomsRequest(String start, String end) {
        this.start = start;
        this.end = end;
    }

    // Class constructor
    public void formatToSend() {
        parseDateTimeToOutput();
    }

    // Additional methods
    private void parseDateTimeToOutput() {
        DateTime dateTime = new DateTime(start, end);

        start = dateTime.getOutputStart();
        end = dateTime.getOutputEnd();
    }
}
