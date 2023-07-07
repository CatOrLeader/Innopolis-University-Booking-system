package Bot.Dialog;

import Bot.Dialog.Data.BotState;
import Bot.Dialog.Data.UserData;
import Bot.Dialog.Handlers.Independent.BookInBotHandler;
import Bot.Dialog.Handlers.Independent.BookingConfirmationHandler;
import Bot.Dialog.Handlers.Independent.GoToMenuHandler;
import Bot.Dialog.Handlers.Independent.SwitchLanguageHandler;
import Bot.Dialog.Handlers.IndependentHandler;
import Bot.Dialog.Handlers.MaybeResponse;
import Bot.Dialog.Handlers.Response;
import Bot.Dialog.Handlers.State.*;
import Bot.Dialog.Handlers.StateHandler;
import com.pengrad.telegrambot.model.Update;

import javax.mail.NoSuchProviderException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to manage all the incoming updates within
 * the dialog with user.
 */
public class UpdatesManager {
    private final Map<BotState, StateHandler> handlerMap;
    private final List<IndependentHandler> independentHandlers;

    public UpdatesManager() {
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
        independentHandlers.add(new BookingConfirmationHandler());
        independentHandlers.add(new BookInBotHandler());
        independentHandlers.add(new SwitchLanguageHandler());
    }

    /**
     * Method to preload state dependent handlers.
     */
    private void preloadStateHandlers() {
        var bookingHandler = new NewBookingHandler();

        AuthenticationHandler authenticationHandler;
        try {
            authenticationHandler = new AuthenticationHandler();
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }

        handlerMap.put(BotState.UNINITIALIZED, new UninitializedHandler());

        handlerMap.put(BotState.INITIAL_LANGUAGE_SETTING, new InitialLanguageHandler());

        handlerMap.put(BotState.ENTER_MAIL, authenticationHandler);
        handlerMap.put(BotState.CODE_AWAITING, authenticationHandler);

        handlerMap.put(BotState.MAIN_MENU, new MainMenuHandler());

        handlerMap.put(BotState.LIST_OF_RESERVATIONS, new UserBookingsHandler());

        handlerMap.put(BotState.BOOKING_TIME_AWAITING, bookingHandler);
        handlerMap.put(BotState.BOOKING_DURATION_AWAITING, bookingHandler);
        handlerMap.put(BotState.ROOM_AWAITING, bookingHandler);
        handlerMap.put(BotState.BOOKING_TITLE_AWAITING, bookingHandler);
    }

    /**
     * Method to create request for bot to answer on user's update.
     *
     * @param update given update.
     * @param data   data of user who send update.
     * @return answer generated for bot.
     */
    public Response handle(Update update, UserData data) {
        var independentResponse = handleIndependently(update, data);
        Response response;

        if (!independentResponse.hasResponse()) {
            var stateHandler = handlerMap.get(data.getDialogState());
            response = stateHandler.handle(update, data);
            return response;
        } else {
            return independentResponse.getResponse();
        }
    }

    /**
     * Method to try handle request independently of current state
     *
     * @param update incoming update
     * @param data   user data
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
}
