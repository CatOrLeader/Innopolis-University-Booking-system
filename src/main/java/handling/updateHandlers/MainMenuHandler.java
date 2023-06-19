package handling.updateHandlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import handling.StateHandler;
import handling.Response;
import handling.context.BotState;

import config.Text;

public class MainMenuHandler extends StateHandler {
    @Override
    public Response handle(Update update) {
        if (update.message() == null) {
            return new Response(BotState.MAIN_MENU_STATE);
        }
        var chatId = update.message().chat().id();
        var text = update.message().text();
        if (text.equals(Text.BookRoomBtn())) {
            return new Response(BotState.MAIN_MENU_STATE,
                    new SendMessage(chatId,
                            Text.BookRoomMsg_Answer()));
        } else if (text.equals(Text.CheckBookingsBtn())) {
            return new Response(BotState.MAIN_MENU_STATE, new SendMessage(chatId,
                    Text.CheckBookingsMsg_Answer()));
        } else {
            return new Response(BotState.MAIN_MENU_STATE);
        }
    }
}
