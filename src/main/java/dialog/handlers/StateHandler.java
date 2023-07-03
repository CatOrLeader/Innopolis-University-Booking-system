package dialog.handlers;

import com.pengrad.telegrambot.model.Update;
import dialog.data.UserData;

/**
 * Class describing a handler for a certain state
 * for ALL updates on it.
 * Certain instance of it will be mapped to
 * corresponding BotState in GlobalUpdatesHandler.
 */
public abstract class StateHandler {
    /**
     * Method to handle incoming update for user with given
     * external data.
     *
     * @param incomingUpdate update from user
     * @param data           external user data (preferred language, email, ....)
     * @return instance of Response to allow bot answer this request
     * and update state of the dialog
     */
    public abstract Response handle(Update incomingUpdate, UserData data);
}
