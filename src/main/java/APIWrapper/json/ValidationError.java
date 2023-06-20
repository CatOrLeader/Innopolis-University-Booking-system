package APIWrapper.json;

public class ValidationError {
    public String loc;
    public String msg;
    public String type;

    public ValidationError(String loc, String msg, String type) {
        this.loc = loc;
        this.msg = msg;
        this.type = type;
    }
}
