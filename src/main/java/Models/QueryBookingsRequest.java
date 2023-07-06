package Models;

public class QueryBookingsRequest {
    public BookingsFilter filter;

    public QueryBookingsRequest(BookingsFilter filter) {
        this.filter = filter;
    }

    // Class constructor
    public void formatToSend() {
        filter.formatToSend();
    }
}
