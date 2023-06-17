package handling.updateHandlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import handling.context.BotState;
import handling.ConcreteHandler;
import handling.Response;

import config.Text;

/**
 * Handler for "NO_STATE" state.
 * TODO: Not actual implementation, just an example
 */
public class NoStateHandler extends ConcreteHandler {
    @Override
    public Response handle(Update update) {
        if (update.message() == null) {
            return new Response(null, BotState.NO_STATE);
        }
        var chatId = update.message().chat().id();
        var botResponse = new SendMessage(chatId,
                Text.NewbiesMsg());

        botResponse.replyMarkup(new ReplyKeyboardMarkup(
                new KeyboardButton(Text.BookRoomBtn()),
                new KeyboardButton(Text.CheckBookingsBtn())
        ).resizeKeyboard(true));

        var nextState = BotState.MAIN_MENU_STATE;
        return new Response(botResponse, nextState);
    }
}
