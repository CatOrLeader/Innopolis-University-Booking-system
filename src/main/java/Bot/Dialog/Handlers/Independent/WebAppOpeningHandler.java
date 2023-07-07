package Bot.Dialog.Handlers.Independent;

import Bot.Dialog.Config.IText;
import Bot.Dialog.Data.UserData;
import Bot.Dialog.Handlers.IndependentHandler;
import Bot.Dialog.Handlers.MaybeResponse;
import Bot.Dialog.Handlers.Response;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class WebAppOpeningHandler extends IndependentHandler {
    @Override
    public MaybeResponse handle(Update incomingUpdate, UserData data) {
        var msg = incomingUpdate.message();
        var lang = data.getLang();
        var usr = data.getUserId();

        if (!data.isAuthorized() || msg == null) {
            return new MaybeResponse();
        }

        if (!isWebAppOpening(msg.text(), lang)) {
            return new MaybeResponse();
        }

        var botMessage = new SendMessage(usr,
                "");
        return new MaybeResponse(new Response(data, botMessage));
    }

    /**
     * Method to check whether given command corresponds to the pushing on the WebApp button
     *
     * @param text given command
     * @return true if it is 'WebApp button' command, false - otherwise
     */
    private boolean isWebAppOpening(String text, IText lang) {
        return text.strip().equals(lang.openWebAppBtn());
    }
}
