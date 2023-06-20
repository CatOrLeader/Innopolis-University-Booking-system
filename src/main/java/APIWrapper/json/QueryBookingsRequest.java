package APIWrapper.json;

public class QueryBookingsRequest {
    public BookingsFilter filter;

    public QueryBookingsRequest(BookingsFilter filter) {
        this.filter = filter;
    }
}
