package Utilities;

import Bot.Dialog.Data.BotState;
import Bot.Dialog.Data.UserBooking;
import Bot.Dialog.Data.UserBookingManager;
import Bot.Dialog.Data.UserData;
import Bot.Dialog.Handlers.Response;
import Database.Controllers.RoomController;
import Models.Booking;
import Models.Room;
import com.pengrad.telegrambot.request.SendMessage;

public class BookingRoomHelper {
    private final RoomController roomData = new RoomController();
    private final UserBookingManager bookingManager = new UserBookingManager();

    public Response processResponse(Booking response, UserData data) {
        var lang = data.getLang();
        var user = data.getUserId();

        SendMessage botMessage;
        if (response == null) {
            botMessage =
                    new SendMessage(user,
                            lang.bookedUnsuccessfully());
        } else {
            botMessage = new SendMessage(user,
                    lang.bookedSuccessfully(response.title,
                            response.room.name,
                            DateTime.formatToConvenient(response.start),
                            DateTime.formatToConvenient(response.end)));
            bookingManager.addBooking(new UserBooking(response, user, false));
        }
        botMessage = botMessage.replyMarkup(lang.mainMenuMarkup());
        data.setDialogState(BotState.MAIN_MENU);
        return new Response(data, botMessage);
    }

    // Utils methods

    /**
     * Find room instance by its id
     *
     * @param roomId given id
     * @return room (it is supposed that given id always correct)
     */
    public Room takeRoomById(String roomId) {
        return roomData.getRoomData(roomId);
    }

    /**
     * Method to return to menu due to unexpected error.
     *
     * @param data user data
     * @return response to return to menu
     */
    public Response abnormalMenuReturn(UserData data) {
        var usr = data.getUserId();
        var lang = data.getLang();

        data.setDialogState(BotState.MAIN_MENU);
        var msg = new SendMessage(usr,
                lang.unexpectedErrorGoToMenu()).replyMarkup(lang.mainMenuMarkup());
        return new Response(data, msg);
    }
}
