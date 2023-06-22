public class Main {
    public static void main(String[] args) {
        var bookingBot = new BookingBot(System.getenv("BOT_TOKEN2"));
        bookingBot.listen();
    }
}
