package handling.stateHandlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import handling.Response;
import handling.StateHandler;
import handling.userData.BotState;
import handling.userData.UserData;

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
