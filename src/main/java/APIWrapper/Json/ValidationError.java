package APIWrapper.Json;

public class ValidationError {
    public final String loc;
    public final String msg;
    public final String type;

    public ValidationError(String loc, String msg, String type) {
        this.loc = loc;
        this.msg = msg;
        this.type = type;
    }
}
