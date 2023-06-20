package handling.stateHandlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import handling.Response;
import handling.StateHandler;
import handling.userData.BotState;
import handling.userData.UserData;

public class EnterEmailHandler extends StateHandler {
    @Override
    public Response handle(Update incomingUpdate, UserData data) {
        var message = incomingUpdate.message();
        if (message == null) {
            return new Response(data);
        }
        var userEmail = message.text();
        var lang = data.getLang();
        /* TODO: validate email, send code on it, ..... But now we do not do it.
                Also probably omit request below.
         */
        data.setEmail(userEmail);
        data.setDialogState(BotState.CODE_AWAITING);
        var botMessage = new SendMessage(data.getUserId(), lang.verificationCodeSent());
        return new Response(data, botMessage);
    }
}
