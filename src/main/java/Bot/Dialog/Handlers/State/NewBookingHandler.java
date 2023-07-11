package Bot.Dialog.Handlers.State;

import Bot.Dialog.Data.BotState;
import Bot.Dialog.Data.UserData;
import Bot.Dialog.Handlers.Response;
import Bot.Dialog.Handlers.StateHandler;
import Models.Booking;
import Models.GetFreeRoomsRequest;
import Models.Room;
import Utilities.DateTime;
import Utilities.Services;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * States Group handler
 */
public class NewBookingHandler extends StateHandler {
    private final Map<Long, Booking> bookingInfo = new HashMap<>();

    public NewBookingHandler() {
        preloadRoomsFromApi();
    }

    private void preloadRoomsFromApi() {
        var rooms = Services.outlook.getAllBookableRooms();
        for (Room room : rooms) {
            Services.roomController.addOrUpdateRoom(room);
        }
    }

    @Override
    public Response handle(Update incomingUpdate, UserData data) {
        switch (data.getDialogState()) {
            case BOOKING_TIME_AWAITING -> {
                return handleBookingTime(incomingUpdate, data);
            }
            case BOOKING_DURATION_AWAITING -> {
                return handleBookingDuration(incomingUpdate, data);
            }
            case ROOM_AWAITING -> {
                return handleRoom(incomingUpdate, data);
            }
            case BOOKING_TITLE_AWAITING -> {
                return handleTitle(incomingUpdate, data);
            }
            default -> {
                return new Response(data);
            }
        }
    }

    /**
     * Handle entered booking title
     *
     * @param update incoming update
     * @param data   user data
     * @return bot response
     */
    private Response handleTitle(Update update, UserData data) {
        var message = update.message();
        if (message == null) {
            return new Response(data);
        }
        var user = data.getUserId();
        var info = bookingInfo.get(user);

        // Data loss
        if (info == null) {
            return abnormalMenuReturn(data);
        }

        info.title = message.text();

        return info.post();
    }

    /**
     * Handle user request for room choose
     *
     * @param update incoming update
     * @param data   user data
     * @return bot response
     */
    private Response handleRoom(Update update, UserData data) {
        var query = update.callbackQuery();
        if (query == null) {
            return new Response(data);
        }

        var user = data.getUserId();
        var lang = data.getLang();
        var roomId = query.data();
        var chatId = query.message().chat().id();
        var msgId = query.message().messageId();
        var info = bookingInfo.get(user);

        try {
            info.room = Services.roomController.getRoomData(roomId);
            assert info.room != null;
            var updateMessage = new EditMessageText(chatId, msgId, lang.chosenRoom(info.room.name));
            var botMessage = new SendMessage(
                    user,
                    lang.bookingTitle());
            data.setDialogState(BotState.BOOKING_TITLE_AWAITING);
            return new Response(data, botMessage, updateMessage);
        } catch (Exception e) { // if callback was from another message or data loss
            return abnormalMenuReturn(data);
        }
    }

    /**
     * Handle user request for setting new booking duration
     *
     * @param update incoming update
     * @param data   user data
     * @return bot response
     */
    private Response handleBookingDuration(Update update, UserData data) {
        var query = update.callbackQuery();
        if (query == null) {
            return new Response(data);
        }

        var user = data.getUserId();
        var chatId = query.message().chat().id();
        var msgId = query.message().messageId();
        var lang = data.getLang();
        var info = bookingInfo.get(user);

        try {
            info.duration = Integer.parseInt(query.data());
        } catch (Exception e) { // data loss
            return abnormalMenuReturn(data);
        }

        var updateMessage =
                new EditMessageText(
                        chatId,
                        msgId,
                        lang.chosenBookingTime(
                                info.start,
                                String.valueOf(info.duration))
                );

        var userRooms = Services.outlook.getAllFreeRooms(
                new GetFreeRoomsRequest(info.start, info.duration));

        if (userRooms.isEmpty()) {
            data.setDialogState(BotState.MAIN_MENU);
            return new Response(data, new SendMessage(user, data.getLang().noAvailableRooms()).
                    replyMarkup(lang.mainMenuMarkup()), updateMessage);
        } else {
            var keyboardWithRooms = lang.availableRoomsKeyboard(userRooms);
            data.setDialogState(BotState.ROOM_AWAITING);
            return new Response(data, new SendMessage(user, data.getLang().hereAvailableRooms()).
                    replyMarkup(keyboardWithRooms), updateMessage);
        }
    }

    /**
     * Handle user request with new booking title
     *
     * @param update incoming update
     * @param data   user data
     * @return bot response
     */
    private Response handleBookingTime(Update update, UserData data) {
        if (update.message() == null) {
            return new Response(data);
        }
        var msg = update.message();
        var usr = data.getUserId();
        var lang = data.getLang();

        var maybeDate = msg.text().strip();

        Boolean isValidated = DateTime.isValid(maybeDate);
        if (isValidated == null || !isValidated) {
            return new Response(data, new SendMessage(usr, lang.invalidBookingTime()));
        }

        bookingInfo.put(usr, new Booking());
        bookingInfo.get(usr).owner_email = data.getEmail();
        bookingInfo.get(usr).userId = usr;
        bookingInfo.get(usr).start = msg.text();

        var botMsg =
                new SendMessage(usr, lang.
                        chooseBookingDuration()).
                        replyMarkup(lang.bookingDurations());

        data.setDialogState(BotState.BOOKING_DURATION_AWAITING);
        return new Response(data, botMsg);
    }

    // Utils methods

    /**
     * Method to return to menu due to unexpected error.
     *
     * @param data user data
     * @return response to return to menu
     */
    private Response abnormalMenuReturn(UserData data) {
        var usr = data.getUserId();
        var lang = data.getLang();

        data.setDialogState(BotState.MAIN_MENU);
        var msg = new SendMessage(usr,
                lang.unexpectedErrorGoToMenu()).replyMarkup(lang.mainMenuMarkup());
        return new Response(data, msg);
    }
}
