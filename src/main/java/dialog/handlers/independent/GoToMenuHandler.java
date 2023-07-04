package dialog.handlers.independent;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dialog.data.BotState;
import dialog.data.UserData;
import dialog.handlers.IndependentHandler;
import dialog.handlers.MaybeResponse;
import dialog.handlers.Response;

/**
 * Independent handler for transition to menu.
 */
public class GoToMenuHandler extends IndependentHandler {
    @Override
    public MaybeResponse handle(Update incomingUpdate, UserData data) {
        var msg = incomingUpdate.message();
        var lang = data.getLang();
        var usr = data.getUserId();

        if (!data.isAuthorized() || msg == null) {
            return new MaybeResponse();
        }

        if (!isMenuCommand(msg.text())) {
            return new MaybeResponse();
        }

        data.setDialogState(BotState.MAIN_MENU);
        var botMessage = new SendMessage(usr,
                lang.abortAndToMenu()).replyMarkup(lang.mainMenuMarkup());
        return new MaybeResponse(new Response(data, botMessage));
    }

    /**
     * Method to check whether given command corresponds to '/menu'
     *
     * @param text given command
     * @return true if it is '/menu' command, false - otherwise
     */
    private boolean isMenuCommand(String text) {
        return text.strip().equals("/menu");
    }
}
