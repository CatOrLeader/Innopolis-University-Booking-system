package APIWrapper.json;

public class Booking {
    public String id;
    public String title;
    public String start;
    public String end;
    public Room room;
    public String owner_email;

    public Booking(String id, String title, String start, String end, Room room, String owner_email) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.room = room;
        this.owner_email = owner_email;
    }
}
