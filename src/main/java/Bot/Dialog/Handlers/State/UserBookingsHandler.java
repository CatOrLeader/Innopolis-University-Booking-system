package Bot.Dialog.Handlers.State;

import Bot.Dialog.Data.BotState;
import Bot.Dialog.Data.UserData;
import Bot.Dialog.Handlers.Response;
import Bot.Dialog.Handlers.StateHandler;
import Utilities.Services;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.regex.Pattern;

public class UserBookingsHandler extends StateHandler {
    @Override
    public Response handle(Update incomingUpdate, UserData data) {
        var query = incomingUpdate.callbackQuery();
        if (query == null) {
            return new Response(data);
        }
        if (query.data().startsWith("back")) {
            return handleBack(incomingUpdate, data);
        } else if (query.data().startsWith("cancel")) {
            return handleCancel(incomingUpdate, data);
        } else if (query.data().startsWith("info")) {
            return handleInfo(incomingUpdate, data);
        } else {
            return new Response(data);
        }
    }

    /**
     * Handle request for pop-up notification with booking info
     *
     * @param update incoming update
     * @param data   user data
     * @return bot response
     */
    private Response handleInfo(Update update, UserData data) {
        var lang = data.getLang();

        var query = update.callbackQuery();
        var text = query.data();

        var pattern = Pattern.compile("(\\d+)");
        var matches = pattern.matcher(text);

        matches.find();
        var infoId = matches.group();

        // TODO: join request to api and database
        var booking = Services.outlook.getBookingById(infoId);

        AnswerCallbackQuery answer = new AnswerCallbackQuery(query.id()).
                text(lang.sorryError()).showAlert(true).cacheTime(0);
        if (booking != null) {
            answer = new AnswerCallbackQuery(query.id()).text(
                    lang.fullBookingInfo(booking)
            ).showAlert(true);
        }

        return new Response(data, answer);
    }

    /**
     * Handle user request for booking cancellation
     *
     * @param update incoming update
     * @param data   user data
     * @return bot response
     */
    private Response handleCancel(Update update, UserData data) {
        var text = update.callbackQuery().data();
        var pattern = Pattern.compile("(\\d+)");
        var matches = pattern.matcher(text);

        matches.find();
        var cancelId = matches.group();

        // REMOVING BOOKING FROM OUR DATABASE
        Services.bookingController.removeBooking(cancelId);

        Services.outlook.deleteBooking(cancelId);
        var bookings = Services.outlook.getBookingsByUser(data.getEmail());

        var msgId = update.callbackQuery().message().messageId();
        var usr = data.getUserId();
        var lang = data.getLang();

        var bookingsKb = lang.userBookings(bookings);
        var edit =
                new EditMessageText(
                        usr,
                        msgId,
                        lang.actualBookings(bookings)
                ).replyMarkup(bookingsKb);

        return new Response(data, edit);
    }

    /**
     * Handle user request to go back from bookings list
     *
     * @param update incoming update
     * @param data   user data
     * @return bot response
     */
    private Response handleBack(Update update, UserData data) {
        var usr = data.getUserId();
        var lang = data.getLang();
        data.setDialogState(BotState.MAIN_MENU);
        var edit =
                new EditMessageText(
                        usr,
                        update.callbackQuery().message().messageId(),
                        lang.bookingInterfaceClosed());
        var botMsg =
                new SendMessage(
                        usr,
                        lang.goToMenu()
                ).replyMarkup(lang.mainMenuMarkup());
        return new Response(data, edit, botMsg);
    }
}
