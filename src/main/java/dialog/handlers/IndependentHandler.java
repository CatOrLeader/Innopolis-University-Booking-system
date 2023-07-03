package dialog.handlers;

import com.pengrad.telegrambot.model.Update;
import dialog.data.UserData;

/**
 * Class that describes independent (from state) handler.
 */
public abstract class IndependentHandler {
    /**
     * Method to handle incoming update
     *
     * @param incomingUpdate update from user
     * @param data           user data
     * @return MaybeResponse instance. Contains Response if handled
     */
    public abstract MaybeResponse handle(Update incomingUpdate, UserData data);
}
