package Bot.Dialog.Handlers.State;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import Bot.Dialog.Data.BotState;
import Bot.Dialog.Data.UserData;
import Bot.Dialog.Handlers.Response;
import Bot.Dialog.Handlers.StateHandler;

/**
 * Handler for uninitialized user's state.
 */
public class UninitializedHandler extends StateHandler {
    @Override
    public Response handle(Update incomingUpdate, UserData data) {
        var message = incomingUpdate.message();
        var lang = data.getLang();

        if (message == null) {
            return new Response(data);
        }
        var botMessage = new SendMessage(data.getUserId(), lang.initial());
        data.setDialogState(BotState.ENTER_MAIL);
        return new Response(data, botMessage);
    }
}
