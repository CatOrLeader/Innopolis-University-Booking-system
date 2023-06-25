import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import dialog.UpdatesManager;

import java.util.Arrays;

/**
 * Class describing booking bot functionality.
 */
public class BookingBot {
    private final TelegramBot bot;
    private final UpdatesManager updatesHandler;
    public BookingBot(String token) {
        bot = new TelegramBot(token);
        updatesHandler = new UpdatesManager();
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
     * There may be multiple response requests on one update.
     * @param update incoming update.
     */
    private void process(Update update) {
        // TODO: try-catch
        var responses = updatesHandler.handle(update);
        Arrays.stream(responses).forEach(bot::execute);
    }
}
