package handling.stateHandlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import config.IText;
import handling.Response;
import handling.StateHandler;
import handling.userData.BotState;
import handling.userData.UserData;

public class CodeAwaitingHandler extends StateHandler {
    @Override
    public Response handle(Update incomingUpdate, UserData data) {
        var message = incomingUpdate.message();
        if (message == null) {
            return new Response(data);
        }
        // TODO: check code
        data.setDialogState(BotState.MAIN_MENU);
        var botMessage =
                new SendMessage(data.getUserId(), data.getLang().authorized()).
                        replyMarkup(buildMarkup(data.getLang()));
        return new Response(data, botMessage);
    }

    private ReplyKeyboardMarkup buildMarkup(IText lang) {
        return new ReplyKeyboardMarkup(
                new KeyboardButton(lang.newBookingBtn()), new KeyboardButton(lang.myReservationsBtn())
        ).resizeKeyboard(true);
    }
}
