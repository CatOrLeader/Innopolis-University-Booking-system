package Models;

import Bot.Dialog.Config.IText;
import Bot.Dialog.Data.BotState;
import Bot.Dialog.Data.UserData;
import Bot.Dialog.Handlers.Response;
import Utilities.DateTime;
import Utilities.Services;
import com.pengrad.telegrambot.request.SendMessage;

public class Booking {
    // Exposed fields
    public String id;
    public String title;
    public String start;
    public String end;
    public Room room;
    public String owner_email;

    // Hidden fields
    public transient long userId;
    public transient boolean isConfirmed = false;
    public transient int duration;

    public Booking(
            String id,
            long userId,
            String title,
            String start,
            String end,
            String roomId,
            String owner_email,
            boolean isConfirmed
    ) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.start = start;
        this.end = end;
        this.room = Services.roomController.getRoomData(roomId);
        this.owner_email = owner_email;
        this.isConfirmed = isConfirmed;
    }

    public Booking(String webAppData, UserData userData) throws Exception {
        Booking temp = Services.gson.fromJson(webAppData, Booking.class);

        this.title = temp.title;
        this.start = temp.start;
        this.end = temp.end;
        room = Services.roomController.getRoomData(temp.room.id);

        userId = userData.getUserId();
        owner_email = userData.getEmail();
    }

    public Booking() {

    }

    // Class constructors
    public BookRoomRequest toBookRoomRequest() {
        if (end == null) {
            return new BookRoomRequest(
                    title,
                    start,
                    duration,
                    owner_email
            );
        } else {
            return new BookRoomRequest(
                    title,
                    start,
                    end,
                    owner_email
            );
        }
    }

    // Main methods to POST booking on the server and synchronise it with the local DB
    public Response post() {
        Booking response = Services.outlook.bookRoom(room.id, toBookRoomRequest());
        UserData userData = Services.userDataController.getUserData(userId);

        long userId = userData.getUserId();
        IText lang = userData.getLang();

        SendMessage botMessage;
        if (response == null) {
            botMessage =
                    new SendMessage(userId,
                            lang.bookedUnsuccessfully());
        } else {
            botMessage = new SendMessage(userId,
                    lang.bookedSuccessfully(response.title,
                            response.room.name,
                            DateTime.formatToConvenient(response.start),
                            DateTime.formatToConvenient(response.end)));
            Services.bookingController.addOrUpdateBooking(response.complete(this));
        }
        botMessage = botMessage.replyMarkup(lang.mainMenuMarkup());
        userData.setDialogState(BotState.MAIN_MENU);
        return new Response(userData, botMessage);
    }

    // Utils methods
    private Booking complete(Booking origin) {
        if (id == null) id = origin.id;
        if (userId == 0) userId = origin.userId;
        return this;
    }
}
