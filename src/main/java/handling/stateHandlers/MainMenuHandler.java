package handling.stateHandlers;

import com.pengrad.telegrambot.model.Update;
import handling.Response;
import handling.StateHandler;
import handling.userData.UserData;

public class MainMenuHandler extends StateHandler {
    @Override
    public Response handle(Update incomingUpdate, UserData data) {
        var message = incomingUpdate.message();
        var lang = data.getLang();
        if (message == null) {
            return new Response(data);
        } else if (message.text().equals(data.getLang().myReservations())) {
            return handleReservations(data);
        } else if (message.text().equals(data.getLang().newBooking())) {
            return handleNewBooking(data);
        } else {
            return new Response(data);
        }
    }

    // TODO: complete full handling
    private Response handleReservations(UserData data) {
        return new Response(data);
    }

    private Response handleNewBooking(UserData data) {
        return new Response(data);
    }
}
