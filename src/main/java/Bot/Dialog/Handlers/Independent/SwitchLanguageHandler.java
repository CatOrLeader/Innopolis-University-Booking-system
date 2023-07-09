package Bot.Dialog.Handlers.Independent;

import Bot.Dialog.Config.EnglishText;
import Bot.Dialog.Config.RussianText;
import Bot.Dialog.Data.BotState;
import Bot.Dialog.Data.UserData;
import Bot.Dialog.Handlers.IndependentHandler;
import Bot.Dialog.Handlers.MaybeResponse;
import Bot.Dialog.Handlers.Response;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class SwitchLanguageHandler extends IndependentHandler {
    @Override
    public MaybeResponse handle(Update incomingUpdate, UserData data) {
        if (!isSwitchLanguageCommand(incomingUpdate)) {
            return new MaybeResponse();
        }
        var usr = data.getUserId();
        var lang = data.getLang();
        if (lang instanceof EnglishText) {
            lang = new RussianText();
        } else {
            lang = new EnglishText();
        }
        data.setLang(lang);
        data.setDialogState(BotState.MAIN_MENU);
        var msg = new SendMessage(usr, lang.languageChangedAndToMenu()).replyMarkup(lang.mainMenuMarkup());
        return new MaybeResponse(new Response(data, msg));
    }

    private boolean isSwitchLanguageCommand(Update update) {
        return update.message() != null &&
                update.message().text() != null &&
                    update.message().text().strip().equals("/language");
    }
}
