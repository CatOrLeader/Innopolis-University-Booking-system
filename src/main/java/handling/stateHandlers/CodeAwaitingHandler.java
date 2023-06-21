package handling.stateHandlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import handling.Keyboards;
import handling.Response;
import handling.StateHandler;
import handling.userData.BotState;
import handling.userData.UserData;

public class CodeAwaitingHandler extends StateHandler {
    @Override
    public Response handle(Update incomingUpdate, UserData data) {
        var message = incomingUpdate.message();
        if (message == null) {
            return new Response(data);
        }
        // TODO: check code
        data.setDialogState(BotState.MAIN_MENU);
        var botMessage =
                new SendMessage(data.getUserId(), data.getLang().authorized()).
                        replyMarkup(Keyboards.mainMenuMarkup(data.getLang()));
        return new Response(data, botMessage);
    }
}
