import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.schedule.Schedules;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import dialog.UpdatesManager;
import dialog.config.EnglishText;
import dialog.data.BookingReminder;
import dialog.data.BookingDataManager;

import java.time.Duration;
import java.util.Arrays;

/**
 * Class describing booking bot functionality.
 */
public class BookingBot {
    private final TelegramBot bot;
    private final UpdatesManager updatesHandler;
    private final BookingDataManager bookingManager = new BookingDataManager();
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

    public void notifyUpcomingBookings() {
        var scheduler = new Scheduler();
        scheduler.schedule(() -> {
            bookingManager.getBookingsToConfirm().forEach(this::notifyBooking);
        }, Schedules.fixedDelaySchedule(Duration.ofMinutes(1)));
    }

    private void notifyBooking(BookingReminder bookingReminder) {
        var booking = bookingReminder.getBooking();
        var usr = bookingReminder.getUserId();
        var request = new SendMessage(
                usr,
                new EnglishText().upcomingBooking(booking)
        );
        bot.execute(request);
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
