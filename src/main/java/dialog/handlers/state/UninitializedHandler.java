package dialog.handlers.state;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dialog.handlers.Response;
import dialog.handlers.StateHandler;
import dialog.userData.BotState;
import dialog.userData.UserData;

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
