import Bot.BookingBot;
import Utilities.Services;

public class Main {
    public static void main(String[] args) {
        var bookingBot = new BookingBot(Services.getEnv("BOT_TOKEN"));
        bookingBot.skipUpdates(true);
        bookingBot.notifyBookings();
        bookingBot.listen();
    }
}
