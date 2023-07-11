package Bot.Dialog.Handlers.State;

import Bot.Dialog.Data.BotState;
import Bot.Dialog.Data.UserData;
import Bot.Dialog.Handlers.Response;
import Bot.Dialog.Handlers.StateHandler;
import Models.Booking;
import Utilities.DateTime;
import Utilities.Services;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;

public class MainMenuHandler extends StateHandler {

    @Override
    public Response handle(Update incomingUpdate, UserData data) {
        if (isMyBookingsTransition(incomingUpdate, data)) {
            return handleBookings(data);
        } else if (isWebAppInfo(incomingUpdate)) {
            return handleWebAppInfo(incomingUpdate, data);
        } else {
            return new Response(data);
        }
    }

    /**
     * Handle transition to list of bookings
     *
     * @param data user data
     * @return bot response
     */
    private Response handleBookings(UserData data) {
        var usr = data.getUserId();
        var lang = data.getLang();

        var bookings = Services.outlook.getBookingsByUser(data.getEmail());
        var actualBookings = lang.actualBookings(bookings);

        var transition = new SendMessage(
                usr,
                lang.goToBookings()
        ).replyMarkup(new ReplyKeyboardRemove());
        var bookingsMsg = new SendMessage(usr, actualBookings).
                replyMarkup(lang.userBookings(bookings));

        data.setDialogState(BotState.LIST_OF_RESERVATIONS);
        return new Response(data, transition, bookingsMsg);
    }

    /**
     * Handle obtaining of info from WebApp
     * @param incomingUpdate incoming update
     * @param data user data
     * @return bot response
     */
    private Response handleWebAppInfo(Update incomingUpdate, UserData data) {
        var msg = incomingUpdate.message();
        var lang = data.getLang();
        var user = data.getUserId();

        if (!data.isAuthorized() || msg == null) {
            return new Response(data);
        }

        try {
            Booking info = new Booking(msg.webAppData().data(), data);

            Boolean isValidated = DateTime.isValid(info.start);
            if (isValidated == null || !isValidated) {
                        return new Response(
                                data, new SendMessage(user, lang.invalidBookingTime())
                        );
            }

            return info.post();
        } catch (Exception e) {
            e.printStackTrace();
            return abnormalMenuReturn(data);
        }
    }

    private boolean isWebAppInfo(Update update) {
        return update.message() != null &&
                update.message().webAppData() != null;
    }

    private boolean isMyBookingsTransition(Update update, UserData data) {
        return update.message() != null &&
                update.message().text() != null &&
                update.message().text().equals(data.getLang().myReservationsBtn());
    }

    /**
     * Method to return to menu due to unexpected error.
     *
     * @param data user data
     * @return response to return to menu
     */
    public Response abnormalMenuReturn(UserData data) {
        var usr = data.getUserId();
        var lang = data.getLang();

        data.setDialogState(BotState.MAIN_MENU);
        var msg = new SendMessage(usr,
                lang.unexpectedErrorGoToMenu()).replyMarkup(lang.mainMenuMarkup());
        return new Response(data, msg);
    }
}
