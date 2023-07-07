package Bot.Dialog.Handlers.Independent;

import Bot.Dialog.Data.BotState;
import Bot.Dialog.Data.UserData;
import Bot.Dialog.Handlers.IndependentHandler;
import Bot.Dialog.Handlers.MaybeResponse;
import Bot.Dialog.Handlers.Response;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

/**
 * Independent handler for transition to menu.
 */
public class GoToMenuHandler extends IndependentHandler {
    @Override
    public MaybeResponse handle(Update incomingUpdate, UserData data) {
        if (!isMenuCommand(incomingUpdate)) {
            return new MaybeResponse();
        }

        var msg = incomingUpdate.message();
        var lang = data.getLang();
        var usr = data.getUserId();

        if (!data.isAuthorized() || msg == null) {
            return new MaybeResponse();
        }

        data.setDialogState(BotState.MAIN_MENU);
        var botMessage = new SendMessage(usr,
                lang.abortAndToMenu()).replyMarkup(lang.mainMenuMarkup());
        return new MaybeResponse(new Response(data, botMessage));
    }

    private boolean isMenuCommand(Update update) {
        return update.message() != null &&
                update.message().text() != null &&
                 update.message().text().strip().equals("/menu");
    }
}
