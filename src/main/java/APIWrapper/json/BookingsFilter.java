package APIWrapper.json;

import APIWrapper.utilities.DateTime;

public class BookingsFilter {
    // Exposed fields
    public String started_at_or_after;
    public String ended_at_or_before;
    public String[] room_id_in;
    public String[] owner_email_in;

    public BookingsFilter(String started_at_or_after, String ended_at_or_before, String[] room_id_in, String[] owner_email_in) {
        this.started_at_or_after = started_at_or_after;
        this.ended_at_or_before = ended_at_or_before;
        this.room_id_in = room_id_in;
        this.owner_email_in = owner_email_in;
    }

    // Class constructor
    public void formatToSend() {
        parseDateTimeToOutput();
    }

    // Additional methods
    private void parseDateTimeToOutput() {
        DateTime dateTime = new DateTime(started_at_or_after, ended_at_or_before);

        started_at_or_after = dateTime.getOutputStart();
        ended_at_or_before = dateTime.getOutputEnd();
    }
}
