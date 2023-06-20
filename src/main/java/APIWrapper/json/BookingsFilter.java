package APIWrapper.json;

// https://stackoverflow.com/questions/63156952/how-to-convert-2020-12-20t000000-000z-to-java-util-date

public class BookingsFilter {
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
}
