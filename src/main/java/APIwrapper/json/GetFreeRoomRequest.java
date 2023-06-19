package APIwrapper.json;

public class GetFreeRoomRequest {
    public String start;
    public String end;

    public GetFreeRoomRequest(String start, String end) {
        this.start = start;
        this.end = end;
    }
}
