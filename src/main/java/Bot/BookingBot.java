package Bot;

import Bot.Dialog.Config.IText;
import Models.Booking;
import Utilities.Services;
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
    private final Set<Integer> updatesToSkip = new HashSet<>();

    public BookingBot(String token) {
        bot = new TelegramBot(token);
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
        var bookingsToCancel = Services.bookingController.getBookingsNow();
        var upcomingBookings = Services.bookingController.getBookingsToNotify();
        bookingsToCancel.forEach(this::removeUnconfirmedBooking);
        upcomingBookings.forEach(this::notifyBooking);
    }

    private void notifyBooking(Booking userBooking) {
        if (userBooking.isConfirmed) {
            return;
        }
        var usr = userBooking.userId;
        var lang = Services.userDataController.getUserData(usr).getLang();
        var msg = new SendMessage(
                usr,
                lang.upcomingBooking(userBooking)
        ).replyMarkup(lang.bookingConfirmation(userBooking));
        bot.execute(msg);
    }

    private void removeUnconfirmedBooking(Booking userBooking) {
        if (userBooking.isConfirmed) {
            return;
        }
        var usr = userBooking.userId;
        var lang = Services.userDataController.getUserData(usr).getLang();
        Services.outlook.deleteBooking(userBooking.id);
        Services.bookingController.removeBooking(userBooking.id);
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
        var data = Services.userDataController.getUserData(userId);
        try {
            var response = Services.updatesManager.handle(update, data);
            Arrays.stream(response.botResponse()).forEach(bot::execute);
            Services.userDataController.setUserData(userId, response.userData());
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
