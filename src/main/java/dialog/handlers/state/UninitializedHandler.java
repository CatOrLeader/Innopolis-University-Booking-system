package dialog.handlers.state;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dialog.data.BotState;
import dialog.data.UserData;
import dialog.handlers.Response;
import dialog.handlers.StateHandler;

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
