import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import handling.GlobalUpdatesHandler;

/**
 * Class describing booking bot functionality.
 */
public class BookingBot {
    private final TelegramBot bot;
    private final GlobalUpdatesHandler updatesHandler;
    public BookingBot(String token) {
        bot = new TelegramBot(token);
        updatesHandler = new GlobalUpdatesHandler();
    }

    /**
     * Method to start listening for user updates.
     */
    public void listen() {
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    /**
     * Method to process all the updates and answer on them.
     * @param update incoming update.
     */
    private void process(Update update) {
        var response = updatesHandler.handle(update);
        if (response != null) {
            bot.execute(response);
        }
    }
}
