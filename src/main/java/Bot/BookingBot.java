package Bot;

import APIWrapper.Requests.Request;
import Bot.Dialog.Config.IText;
import Bot.Dialog.Data.UserBooking;
import Bot.Dialog.Data.UserBookingManager;
import Bot.Dialog.Data.UserDataManager;
import Bot.Dialog.UpdatesManager;
import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.schedule.Schedules;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Class describing booking bot functionality.
 */
public class BookingBot {
    private final TelegramBot bot;
    private final UpdatesManager updatesManager;
    private final UserBookingManager bookingManager;
    private final UserDataManager userManager;
    private final Request outlook = new Request();
    private final Set<Integer> updatesToSkip = new HashSet<>();

    public BookingBot(String token) {
        bot = new TelegramBot(token);
        updatesManager = new UpdatesManager();
        userManager = new UserDataManager();
        bookingManager = new UserBookingManager();
    }

    /**
     * Skip updates that come to bot in offline
     * @param flag true - need to skip, false - process them
     */
    public void skipUpdates(boolean flag) {
        updatesToSkip.clear();
        if (flag) {
            var updates = bot.execute(new GetUpdates()).updates();
            updates.forEach(update -> updatesToSkip.add(update.updateId()));
        }
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

    public void notifyBookings() {
        var scheduler = new Scheduler();
        scheduler.schedule(this::processBookings,
                Schedules.fixedDelaySchedule(Duration.ofMinutes(1)));
    }

    private void processBookings() {
        var bookingsToCancel = bookingManager.getBookingsNow();
        var upcomingBookings = bookingManager.getBookingsToNotify();
        bookingsToCancel.forEach(this::removeUnconfirmedBooking);
        upcomingBookings.forEach(this::notifyBooking);
    }

    private void notifyBooking(UserBooking userBooking) {
        var usr = userBooking.userId;
        var lang = userManager.getUserData(usr).getLang();
        var msg = new SendMessage(
                usr,
                lang.upcomingBooking(userBooking)
        ).replyMarkup(lang.bookingConfirmation(userBooking));
        bot.execute(msg);
    }

    private void removeUnconfirmedBooking(UserBooking userBooking) {
        if (userBooking.isConfirmed) {
            return;
        }
        var usr = userBooking.userId;
        var lang = userManager.getUserData(usr).getLang();
        outlook.deleteBooking(userBooking.id);
        bookingManager.removeBookingById(userBooking.id);
        var msg = new SendMessage(usr, lang.unconfirmedBookingCancel(userBooking));
        bot.execute(msg);
    }

    /**
     * Method to process all the updates and answer on them.
     * There may be multiple response requests on one update.
     *
     * @param update incoming update.
     */
    private void process(Update update) {
        if (needIgnore(update)) {
            return;
        }
        var userId = extractUserId(update);
        if (userId == null) { // if user was not extracted -- no need to handle
            return;
        }
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
     * Check whether this update should be skipped
     * @param update incoming update
     * @return true if this update should be skipped, false otherwise
     */
    private boolean needIgnore(Update update) {
        if (updatesToSkip.contains(update.updateId())) {
            updatesToSkip.remove(update.updateId());
            return true;
        }
        return false;
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
    private Long extractUserId(Update update) {
        if (update.message() != null) {
            return update.message().from().id();
        } else if (update.callbackQuery() != null) {
            return update.callbackQuery().from().id();
        }
        return null; // in order not to handle third-party updates
    }
}
