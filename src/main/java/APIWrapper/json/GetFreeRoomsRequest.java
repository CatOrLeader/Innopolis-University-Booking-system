package APIWrapper.json;

public class GetFreeRoomsRequest {
    public String start;
    public String end;

    public GetFreeRoomsRequest(String start, String end) {
        this.start = start;
        this.end = end;
    }
}
