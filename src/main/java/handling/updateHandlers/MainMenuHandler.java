package handling.updateHandlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import handling.ConcreteHandler;
import handling.Response;
import handling.context.BotState;

public class MainMenuHandler extends ConcreteHandler {
    @Override
    public Response handle(Update update) {
        if (update.message() == null) {
            return new Response(null, BotState.MAIN_MENU_STATE);
        }
        var chatId = update.message().chat().id();
        var text = update.message().text();
        if (text.equals("Book a room")) {
            return new Response(
                    new SendMessage(chatId,
                            "There is no such opportunity yet :("),
                    BotState.MAIN_MENU_STATE);
        } else if (text.equals("Look my rooms")) {
            return new Response(
                    new SendMessage(chatId,
                            "You can not book rooms -> You can not check your reservations :)"),
                    BotState.MAIN_MENU_STATE);
        } else {
            return new Response(null, BotState.NO_STATE);
        }
    }
}
