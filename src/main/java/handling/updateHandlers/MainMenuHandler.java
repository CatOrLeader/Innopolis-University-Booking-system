package handling.updateHandlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import handling.ConcreteHandler;
import handling.Response;
import handling.context.BotState;

import config.Text;

public class MainMenuHandler extends ConcreteHandler {
    @Override
    public Response handle(Update update) {
        if (update.message() == null) {
            return new Response(null, BotState.MAIN_MENU_STATE);
        }
        var chatId = update.message().chat().id();
        var text = update.message().text();
        if (text.equals(Text.BookRoomBtn())) {
            return new Response(
                    new SendMessage(chatId,
                            Text.BookRoomMsg_Answer()),
                    BotState.MAIN_MENU_STATE);
        } else if (text.equals(Text.CheckBookingsBtn())) {
            return new Response(
                    new SendMessage(chatId,
                            Text.CheckBookingsMsg_Answer()),
                    BotState.MAIN_MENU_STATE);
        } else {
            return new Response(null, BotState.NO_STATE);
        }
    }
}
