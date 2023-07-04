package Bot.Dialog.Handlers.Independent;

import APIWrapper.Requests.Request;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.EditMessageText;
import Bot.Dialog.Data.BookingDataManager;
import Bot.Dialog.Data.UserData;
import Bot.Dialog.Handlers.IndependentHandler;
import Bot.Dialog.Handlers.MaybeResponse;
import Bot.Dialog.Handlers.Response;

// TODO: finally get booking by ID
// TODO: handle errors or already finished bookings

public class BookingConfirmationHandler extends IndependentHandler {
    private final BookingDataManager bookingManager = new BookingDataManager();
    private final Request outlook = new Request("http://localhost:3000");
    @Override
    public MaybeResponse handle(Update incomingUpdate, UserData data) {
        var callback = incomingUpdate.callbackQuery();
        if (callback == null) {
            return new MaybeResponse();
        }
        var usr = data.getUserId();
        var lang = data.getLang();
        var text = callback.data();
        var msgId = callback.message().messageId();
        if (isBookingConfirmation(text)) {
            var confirmId = text.split(" ")[1];
            bookingManager.setConfirmed(
                    bookingManager.getBookingById(confirmId));
            var edit = new EditMessageText(usr, msgId, lang.bookingConfirmed());
            return new MaybeResponse(new Response(data, edit));
        } else if (isBookingRevoke(text)) {
            var revokeId = text.split(" ")[1];
            outlook.deleteBooking(revokeId);
            bookingManager.removeBooking(
                    bookingManager.getBookingById(revokeId));
            var edit = new EditMessageText(usr, msgId, lang.bookingRevoked());
            return new MaybeResponse(new Response(data, edit));
        } else {
            return new MaybeResponse();
        }
    }

    private boolean isBookingConfirmation(String data) {
        return data.startsWith("confirm ");
    }

    private boolean isBookingRevoke(String data) {
        return data.startsWith("revoke ");
    }
}
