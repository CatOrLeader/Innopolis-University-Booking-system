package handling;

import com.pengrad.telegrambot.model.Update;

/**
 * Class describing a handler for a certain state
 * for ALL updates on it.
 * Certain instance of it will be mapped to
 * corresponding BotState in GlobalUpdatesHandler.
 */
public abstract class StateHandler {
    // TODO: In certain specifications may be other fields to access API, DB...
    public abstract Response handle(Update update);
}
