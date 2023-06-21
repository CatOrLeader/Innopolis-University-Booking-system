package handling.stateHandlers;

import APIWrapper.json.BookRoomRequest;
import APIWrapper.json.Room;
import APIWrapper.requests.Request;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import handling.Keyboards;
import handling.Response;
import handling.StateHandler;
import handling.userData.BotState;
import handling.userData.UserData;

import java.util.HashMap;
import java.util.Map;

/**
 * States Group handler
 */
// TODO: implement a class to work with user booking parameters at parse it to request
public class NewBookingHandler extends StateHandler {
    private final Map<String, BookRoomRequest> userRequests = new HashMap<>();
    private final Request outlook = new Request("http://localhost:3000");
    private final Room[] rooms =
            outlook.getAllBookableRooms().toArray(Room[]::new);
    @Override
    public Response handle(Update incomingUpdate, UserData data) {
        switch (data.getDialogState()) {
            case BOOKING_TIME_AWAITING -> {
                return handleBookingTime(incomingUpdate, data);
            }
            case BOOKING_DURATION_AWAITING -> {
                return handleBookingDuration(incomingUpdate, data);
            }
            default -> {
                return new Response(data);
            }
        }
    }

    // Handle somehow
    private Response handleBookingDuration(Update update, UserData data) {
        var query = update.callbackQuery();
        if (query == null) {
            return new Response(data);
        }
        var user = data.getUserId();
        var duration = query.data();
        // TODO: properly handle work with time
        userRequests.get(user).end = userRequests.get(user).start + duration;
        // TODO: properly obtain list of available rooms at given time
        var userRooms = rooms;
        if (userRooms.length == 0) {
            data.setDialogState(BotState.MAIN_MENU);
            return new Response(data, new SendMessage(user, data.getLang().noAvailableRooms()).
                    replyMarkup(Keyboards.mainMenuMarkup(data.getLang())));
        } else {
            var keyboardWithRooms = Keyboards.availableRoomsKeyboard(rooms);
            data.setDialogState(BotState.ROOM_AWAITING);
            return new Response(data, new SendMessage(user, data.getLang().hereAvailableRooms()).
                    replyMarkup(keyboardWithRooms));
        }
    }

    private Response handleBookingTime(Update update, UserData data) {
        if (update.message() == null) {
            return new Response(data);
        }
        var msg = update.message();
        var usr = data.getUserId();

        userRequests.put(usr, new BookRoomRequest());
        // TODO: validate user input time
        userRequests.get(usr).start = msg.text();
        var botMsg =
                new SendMessage(usr, data.getLang().
                        chooseBookingDuration()).
                        replyMarkup(Keyboards.bookingDurations());
        data.setDialogState(BotState.BOOKING_DURATION_AWAITING);
        return new Response(data, botMsg);
    }
}
