package Bot.Dialog.Handlers.Independent;

import Bot.Dialog.Data.BotState;
import Bot.Dialog.Data.UserData;
import Bot.Dialog.Handlers.IndependentHandler;
import Bot.Dialog.Handlers.MaybeResponse;
import Bot.Dialog.Handlers.Response;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class BookInBotHandler extends IndependentHandler {
    @Override
    public MaybeResponse handle(Update incomingUpdate, UserData data) {
        if (!isNewBookingCommand(incomingUpdate) || !data.isAuthorized()) {
            return new MaybeResponse();
        }
        var usr = data.getUserId();
        var lang = data.getLang();
        data.setDialogState(BotState.BOOKING_TIME_AWAITING);
        var msg = new SendMessage(usr, lang.chooseBookingTime());
        return new MaybeResponse(new Response(data, msg));
    }

    private boolean isNewBookingCommand(Update update) {
        return update.message() != null &&
                update.message().text() != null &&
                    update.message().text().strip().equals("/book");
    }
}
