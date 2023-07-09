package Bot.Dialog.Handlers.Independent;

import APIWrapper.Requests.Request;
import Bot.Dialog.Data.UserBookingManager;
import Bot.Dialog.Data.UserData;
import Bot.Dialog.Handlers.IndependentHandler;
import Bot.Dialog.Handlers.MaybeResponse;
import Bot.Dialog.Handlers.Response;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.EditMessageText;

public class BookingConfirmationHandler extends IndependentHandler {
    private final UserBookingManager bookingManager = new UserBookingManager();
    private final Request outlook = new Request();
    @Override
    public MaybeResponse handle(Update incomingUpdate, UserData data) {
        if (!(isBookingConfirmation(incomingUpdate) || isBookingRevoke(incomingUpdate))) {
            return new MaybeResponse();
        }
        var callback = incomingUpdate.callbackQuery();
        var usr = data.getUserId();
        var lang = data.getLang();
        var text = callback.data();
        var msgId = callback.message().messageId();
        if (isBookingConfirmation(incomingUpdate)) {
            var confirmId = text.split(" ")[1];
            var edit = new EditMessageText(usr, msgId, lang.bookingDoesNotExist());
            if (bookingManager.getBookingById(confirmId) != null) {
                bookingManager.setConfirmed(confirmId);
                edit = new EditMessageText(usr, msgId, lang.bookingConfirmed());
            }
            return new MaybeResponse(new Response(data, edit));
        } else if (isBookingRevoke(incomingUpdate)) {
            var revokeId = text.split(" ")[1];
            var edit = new EditMessageText(usr, msgId, lang.bookingDoesNotExist());
            if (bookingManager.getBookingById(revokeId) != null) {
                outlook.deleteBooking(revokeId);
                bookingManager.removeBookingById(revokeId);
                edit = new EditMessageText(usr, msgId, lang.bookingRevoked());
            }
            return new MaybeResponse(new Response(data, edit));
        } else {
            return new MaybeResponse();
        }
    }

    private boolean isBookingConfirmation(Update update) {
        return update.callbackQuery() != null && update.callbackQuery().data().startsWith("confirm ");
    }

    private boolean isBookingRevoke(Update update) {
        return update.callbackQuery() != null && update.callbackQuery().data().startsWith("revoke ");
    }
}
