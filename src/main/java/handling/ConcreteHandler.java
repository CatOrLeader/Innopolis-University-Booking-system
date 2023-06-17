package handling;

import com.pengrad.telegrambot.model.Update;

/**
 * Class describing a handler for a concrete update.
 * Certain instance of it will be mapped to
 * corresponding BotState in GlobalUpdatesHandler.
 */
public abstract class ConcreteHandler {
    // TODO: Create static fields to access API, Database, ...
    public abstract Response handle(Update update);
}
