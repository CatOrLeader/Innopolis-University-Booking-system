package Bot.Dialog.Handlers.State;

import Bot.Dialog.Config.EnglishText;
import Bot.Dialog.Config.RussianText;
import Bot.Dialog.Data.BotState;
import Bot.Dialog.Data.UserData;
import Bot.Dialog.Handlers.Response;
import Bot.Dialog.Handlers.StateHandler;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;

public class InitialLanguageHandler extends StateHandler {
    @Override
    public Response handle(Update incomingUpdate, UserData data) {
        if (incomingUpdate.callbackQuery() == null) {
            return new Response(data);
        }
        return switch (incomingUpdate.callbackQuery().data()) {
            case "language eng" -> setEnglish(incomingUpdate, data);
            case "language rus" -> setRussian(incomingUpdate, data);
            default -> new Response(data);
        };
    }

    private Response setRussian(Update update, UserData data) {
        data.setLang(new RussianText());
        var usr = data.getUserId();
        var editMsg = update.callbackQuery().message().messageId();
        var edit = new EditMessageText(usr, editMsg, "Русский язык установлен.");
        data.setDialogState(BotState.ENTER_MAIL);
        return new Response(data, edit, new SendMessage(usr, data.getLang().enterEmail()));
    }

    private Response setEnglish(Update update, UserData data) {
        data.setLang(new EnglishText());
        var usr = data.getUserId();
        var editMsg = update.callbackQuery().message().messageId();
        var edit = new EditMessageText(usr, editMsg, "English language is set.");
        data.setDialogState(BotState.ENTER_MAIL);
        return new Response(data, edit, new SendMessage(usr, data.getLang().enterEmail()));
    }
}
