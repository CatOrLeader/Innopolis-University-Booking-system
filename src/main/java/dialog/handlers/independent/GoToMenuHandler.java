package dialog.handlers.independent;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dialog.handlers.IndependentHandler;
import dialog.handlers.MaybeResponse;
import dialog.handlers.Response;
import dialog.userData.BotState;
import dialog.userData.UserData;

public class GoToMenuHandler extends IndependentHandler {
    @Override
    public MaybeResponse handle(Update incomingUpdate, UserData data) {
        var msg = incomingUpdate.message();
        var state = data.getDialogState();
        var lang = data.getLang();
        var usr = data.getUserId();

        if (notAuthenticated(state) || msg == null) {
            return new MaybeResponse();
        }

        var text = msg.text();
        if (!text.strip().equals("/menu")) {
            return new MaybeResponse();
        }

        data.setDialogState(BotState.MAIN_MENU);
        var botMessage = new SendMessage(usr,
                lang.abortAndToMenu()).replyMarkup(lang.mainMenuMarkup());
        return new MaybeResponse(new Response(data, botMessage));
    }

    private boolean notAuthenticated(BotState state) {
        return state == BotState.UNINITIALIZED ||
                state == BotState.ENTER_MAIL ||
                state == BotState.CODE_AWAITING;
    }
}
