package handling;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import config.EnglishText;
import handling.stateHandlers.*;
import handling.userData.BotState;
import handling.userData.UserData;
import handling.userData.UserDataManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to manage all the incoming updates within
 * the dialog with user.
 */
public class UpdatesManager {
    private final UserDataManager dataManager;
    private final Map<BotState, StateHandler> handlerMap;

    public UpdatesManager() {
        dataManager = new UserDataManager();
        handlerMap = new HashMap<>();
        preloadHandlers();
    }

    /**
     * Method to preload handlers for all states.
     */
    // TODO: Handlers!
    private void preloadHandlers() {
        var bookingHandler = new NewBookingHandler();

        handlerMap.put(BotState.UNINITIALIZED, new UninitializedHandler());
        handlerMap.put(BotState.ENTER_MAIL, new EnterEmailHandler());
        handlerMap.put(BotState.CODE_AWAITING, new CodeAwaitingHandler());
        handlerMap.put(BotState.MAIN_MENU, new MainMenuHandler());

        handlerMap.put(BotState.BOOKING_TIME_AWAITING, bookingHandler);
        handlerMap.put(BotState.BOOKING_DURATION_AWAITING, bookingHandler);
        handlerMap.put(BotState.ROOM_AWAITING, bookingHandler);
        handlerMap.put(BotState.BOOKING_TITLE_AWAITING, bookingHandler);
    }

    /**
     * Method to create request for bot to answer on user's update.
     * @param update given update.
     * @return answer generated for bot.
     */
    public BaseRequest[] handle(Update update) {
        var userId = extractUserId(update);
        if (!dataManager.hasUserData(userId)) {
            var initialData = new UserData(userId, BotState.UNINITIALIZED, null, new EnglishText());
            dataManager.setUserData(userId, initialData);
        }
        var userData = dataManager.getUserData(userId);
        var stateHandler = handlerMap.get(userData.getDialogState());
        var response = stateHandler.handle(update, userData);
        dataManager.setUserData(userId, response.userData());
        return response.botResponse();
    }

    /**
     * Method to get user (that made update) ID for any given update.
     * @param update update.
     * @return user id parsed to string.
     * TODO: Extend variety of updates with sender id
     */
    private Long extractUserId(Update update) {
        if (update.message() != null) {
            return update.message().from().id();
        } else if (update.callbackQuery() != null) {
            return update.callbackQuery().from().id();
        }
        return null;
    }
}
