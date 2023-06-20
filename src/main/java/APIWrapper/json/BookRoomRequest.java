package APIWrapper.json;

public class BookRoomRequest {
    public String title;
    public String start;
    public String end;
    public String owner_email;

    public BookRoomRequest(String title, String start, String end, String owner_email) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.owner_email = owner_email;
    }
}
