package dialog;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import config.EnglishText;
import dialog.handlers.IndependentHandler;
import dialog.handlers.MaybeResponse;
import dialog.handlers.Response;
import dialog.handlers.StateHandler;
import dialog.handlers.independent.GoToMenuHandler;
import dialog.handlers.state.AuthenticationHandler;
import dialog.handlers.state.MainMenuHandler;
import dialog.handlers.state.NewBookingHandler;
import dialog.handlers.state.UninitializedHandler;
import dialog.userData.BotState;
import dialog.userData.UserData;
import dialog.userData.UserDataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to manage all the incoming updates within
 * the dialog with user.
 */
public class UpdatesManager {
    private final UserDataManager dataManager;
    private final Map<BotState, StateHandler> handlerMap;
    private final List<IndependentHandler> independentHandlers;

    public UpdatesManager() {
        dataManager = new UserDataManager();
        handlerMap = new HashMap<>();
        independentHandlers = new ArrayList<>();
        preloadHandlers();
    }

    /**
     * Method to preload handlers for all states.
     */
    // TODO: Handlers!
    private void preloadHandlers() {
        preloadStateHandlers();
        preloadIndependentHandlers();
    }

    /**
     * Method to preload independent handlers.
     */
    private void preloadIndependentHandlers() {
        independentHandlers.add(new GoToMenuHandler());
    }

    /**
     * Method to preload state dependent handlers.
     */
    private void preloadStateHandlers() {
        var bookingHandler = new NewBookingHandler();
        var authenticationHandler = new AuthenticationHandler();

        handlerMap.put(BotState.UNINITIALIZED, new UninitializedHandler());

        handlerMap.put(BotState.ENTER_MAIL, authenticationHandler);
        handlerMap.put(BotState.CODE_AWAITING, authenticationHandler);

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
            var initialData = new UserData(
                    userId,
                    BotState.UNINITIALIZED,
                    null,
                    new EnglishText());
            dataManager.setUserData(userId, initialData);
        }

        var userData = dataManager.getUserData(userId);

        var independentResponse = handleIndependently(update, userData);
        Response response;

        if (!independentResponse.hasResponse()) {
            var stateHandler = handlerMap.get(userData.getDialogState());
            response = stateHandler.handle(update, userData);
        } else {
            response = independentResponse.getResponse();
        }

        dataManager.setUserData(userId, response.userData());
        return response.botResponse();
    }

    /**
     * Method to try handle request independently of current state
     * @param update incoming update
     * @param data user data
     * @return MaybeResponse instance. Contains response if handled
     */
    private MaybeResponse handleIndependently(Update update, UserData data) {
        for (IndependentHandler handler : independentHandlers) {
            var independentResponse = handler.handle(update, data);
            if (independentResponse.hasResponse()) {
                return independentResponse;
            }
        }
        return new MaybeResponse();
    }

    /**
     * Method to get user (that made update) ID for any given update.
     * @param update update.
     * @return user id parsed to string.
     * TODO: Extend variety of updates with sender id
     */
    private long extractUserId(Update update) {
        if (update.message() != null) {
            return update.message().from().id();
        } else {
            return update.callbackQuery().from().id();
        }
    }
}
