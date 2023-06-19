package handling;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import handling.context.BotState;
import handling.context.UserContextHandler;
import handling.updateHandlers.MainMenuHandler;
import handling.updateHandlers.NoStateHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to manage all the incoming updates within
 * the dialog with user.
 */
public class GlobalUpdatesHandler {
    private final UserContextHandler contextHandler;
    private final Map<BotState, StateHandler> handlerMap;

    // TODO: Somehow preload handlers
    public GlobalUpdatesHandler() {
        contextHandler = new UserContextHandler();
        handlerMap = new HashMap<>();
        preloadHandlers();
    }

    /**
     * Method to preload handlers for all states.
     */
    private void preloadHandlers() {
        handlerMap.put(BotState.NO_STATE, new NoStateHandler());
        handlerMap.put(BotState.MAIN_MENU_STATE, new MainMenuHandler());
    }

    /**
     * Method to create request for bot to answer on user's update.
     * @param update given update.
     * @return answer generated for bot.
     */
    public BaseRequest[] handle(Update update) {
        var userId = extractUserId(update);
        var userState = contextHandler.getUserState(userId);
        var stateHandler = handlerMap.get(userState);

        var response = stateHandler.handle(update);
        contextHandler.setUserState(userId, response.nextState());
        return response.botResponse();
    }

    /**
     * Method to get user (that made update) id for any given update.
     * @param update update.
     * @return user id parsed to string.
     * TODO: Extend variety of updates with sender id
     */
    private String extractUserId(Update update) {
        if (update.message() != null) {
            return update.message().from().id().toString();
        } else if (update.callbackQuery() != null) {
            return update.callbackQuery().from().id().toString();
        }
        return null;
    }
}
