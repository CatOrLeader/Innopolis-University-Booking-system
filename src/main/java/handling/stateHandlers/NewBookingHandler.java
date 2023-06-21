package handling.stateHandlers;

import APIWrapper.json.BookRoomRequest;
import APIWrapper.json.Room;
import APIWrapper.requests.Request;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import handling.Response;
import handling.StateHandler;
import handling.userData.BotState;
import handling.userData.UserData;

import java.util.HashMap;
import java.util.Map;

/**
 * States Group handler
 */
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
        return new Response(data);
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
                        replyMarkup(bookingDurations());
        data.setDialogState(BotState.BOOKING_DURATION_AWAITING);
        return new Response(data, botMsg);
    }

    private InlineKeyboardMarkup bookingDurations() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[] {
                        new InlineKeyboardButton("30 min").callbackData("30"),
                        new InlineKeyboardButton("60 min").callbackData("60"),
                        new InlineKeyboardButton("90 min").callbackData("90")
                },
                new InlineKeyboardButton[] {
                        new InlineKeyboardButton("120 min").callbackData("120"),
                        new InlineKeyboardButton("150 min").callbackData("150"),
                        new InlineKeyboardButton("180 min").callbackData("180")
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("❌").callbackData("cancel")
                }
        );
    }
}
