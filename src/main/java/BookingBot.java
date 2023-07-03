import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.schedule.Schedules;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import dialog.UpdatesManager;
import dialog.config.IText;
import dialog.data.BookingDataManager;
import dialog.data.BookingReminder;
import dialog.data.UserDataManager;

import java.time.Duration;
import java.util.Arrays;

/**
 * Class describing booking bot functionality.
 */
public class BookingBot {
    private final TelegramBot bot;
    private final UpdatesManager updatesManager;
    private final BookingDataManager bookingManager;
    private final UserDataManager userManager;
    private int skipUntil = 0;

    public BookingBot(String token) {
        bot = new TelegramBot(token);
        updatesManager = new UpdatesManager();
        userManager = new UserDataManager();
        bookingManager = new BookingDataManager();
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
        scheduler.schedule(
                () ->
                        bookingManager
                                .getBookingsToConfirm()
                                .forEach(this::notifyBooking),
                Schedules.fixedDelaySchedule(Duration.ofMinutes(1)));
    }

    private void notifyBooking(BookingReminder bookingReminder) {
        var booking = bookingReminder.getBooking();
        var usr = bookingReminder.getUserId();
        var lang = userManager.getUserData(usr).getLang();
        var request = new SendMessage(
                usr,
                lang.upcomingBooking(booking)
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
        var userId = extractUserId(update);
        var data = userManager.getUserData(userId);
        try {
            var response = updatesManager.handle(update, data);
            Arrays.stream(response.botResponse()).forEach(bot::execute);
            userManager.setUserData(userId, response.userData());
        } catch (Exception any) {
            excuseProcessCrash(userId, data.getLang());
        }
    }

    /**
     * Method to excuse accidental processes crashes
     */
    private void excuseProcessCrash(long userId, IText lang) {
        var msg = new SendMessage(
                userId,
                lang.sorryError()
        );
        bot.execute(msg);
    }

    /**
     * Method to get user (that made update) ID for any given update.
     *
     * @param update update.
     * @return user id parsed to string.
     * TODO: Extend variety of updates with sender id
     */
    private long extractUserId(Update update) {
        if (update.message() != null) {
            return update.message().from().id();
        } else {
            return update.callbackQuery().from().id();
        }
    }
}
