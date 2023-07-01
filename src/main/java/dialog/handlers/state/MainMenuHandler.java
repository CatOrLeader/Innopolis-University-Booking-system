package dialog.handlers.state;

import APIWrapper.requests.Request;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import dialog.config.EnglishText;
import dialog.config.RussianText;
import dialog.handlers.Response;
import dialog.handlers.StateHandler;
import dialog.userData.BotState;
import dialog.userData.UserData;

public class MainMenuHandler extends StateHandler {
    private final Request outlook = new Request("http://localhost:3000");

    @Override
    public Response handle(Update incomingUpdate, UserData data) {
        var message = incomingUpdate.message();
        if (message == null) {
            return new Response(data);
        } else if (message.text().equals(data.getLang().myReservationsBtn())) {
            return handleBookings(data);
        } else if (message.text().equals(data.getLang().newBookingBtn())) {
            return handleNewBooking(data);
        } else if (message.text().equals(data.getLang().changeLanguage())){
            return changeLanguage(data);
        } else {
            return new Response(data);
        }
    }

    private Response changeLanguage(UserData data) {
        var usr = data.getUserId();
        var lang = data.getLang();
        var newLang = (lang instanceof EnglishText ? new RussianText() : new EnglishText());
        data.setLang(newLang);

        var msg = new SendMessage(usr, newLang.languageChanged()).replyMarkup(newLang.mainMenuMarkup());
        return new Response(data, msg);
    }

    private Response handleBookings(UserData data) {
        var usr = data.getUserId();
        var lang = data.getLang();

        var bookings = outlook.getBookingsByUser(data.getEmail());
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

    private Response handleNewBooking(UserData data) {
        var lang = data.getLang();
        var usr = data.getUserId();

        var botMessage = new SendMessage(
                usr, lang.chooseBookingTime())
                .replyMarkup(new ReplyKeyboardRemove());

        data.setDialogState(BotState.BOOKING_TIME_AWAITING);
        return new Response(data, botMessage);
    }
}
