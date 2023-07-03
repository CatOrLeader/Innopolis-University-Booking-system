import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import dialog.UpdatesManager;

import java.util.Arrays;

import dialog.scheduler.JobScheduler;

/**
 * Class describing booking bot functionality.
 */
public class BookingBot {
    private final TelegramBot bot;
    private final UpdatesManager updatesHandler;
    private int skipUntil = 0;

    public BookingBot(String token) {
        bot = new TelegramBot(token);
        updatesHandler = new UpdatesManager();
    }

    /**
     * Method to skip updates on bot startup.
     * Saves the max Update ID that should be skipped.
     */
    public void skipUpdates() {
        var updates = bot.execute(new GetUpdates()).updates();
        updates.forEach(update -> skipUntil = Math.max(skipUntil, update.updateId()));
    }

    /**
     * Method to start listening for user updates.
     */
    public void listen() {
        JobScheduler jobScheduler = new JobScheduler();

        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                if (update.updateId() > skipUntil) {
                    process(update);
                } else if (update.updateId() == skipUntil) {
                    skipUntil = 0;
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    /**
     * Method to process all the updates and answer on them.
     * There may be multiple response requests on one update.
     *
     * @param update incoming update.
     */
    private void process(Update update) {
        try {
            var responses = updatesHandler.handle(update);
            Arrays.stream(responses).forEach(bot::execute);
        } catch (Exception any) {
            excuseProcessCrash(update);
        }
    }

    /**
     * Method to excuse accidental processes crashes
     *
     * @param update improperly handled update
     */
    private void excuseProcessCrash(Update update) {
        var usr = UpdatesManager.extractUserId(update);
        var msgContent = """
                Sorry, something went wrong...

                Извините, что-то пошло не так...""";
        var msg = new SendMessage(
                usr,
                msgContent
        );
        bot.execute(msg);
    }
}
