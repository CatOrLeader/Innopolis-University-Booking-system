package Utilities;

import Bot.Dialog.Data.UserData;
import Models.Booking;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WebAppMsgParser {
    private final Gson gson;
    private final BookingRoomHelper bookingHelper = new BookingRoomHelper();

    public WebAppMsgParser() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public Booking constructFromWebapp(String webAppData, UserData data) {
        Booking info = parseToBooking(webAppData);
        info.room = bookingHelper.takeRoomById(info.room.id);
        info.owner_email = data.getEmail();

        return info;
    }

    private Booking parseToBooking(String webAppData) {
        return gson.fromJson(webAppData, Booking.class);
    }
}
