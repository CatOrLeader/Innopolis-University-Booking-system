package Bot.Dialog.Handlers.Independent;

import APIWrapper.Requests.Request;
import Bot.Dialog.Data.UserData;
import Bot.Dialog.Handlers.IndependentHandler;
import Bot.Dialog.Handlers.MaybeResponse;
import Bot.Dialog.Handlers.Response;
import Models.Booking;
import Utilities.BookingRoomHelper;
import Utilities.DateTime;
import Utilities.WebAppMsgParser;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class WebAppInfoHandler extends IndependentHandler {
    private final Request outlook = new Request();
    private final WebAppMsgParser parser = new WebAppMsgParser();
    private final BookingRoomHelper bookingHelper = new BookingRoomHelper();

    @Override
    public MaybeResponse handle(Update incomingUpdate, UserData data) {
        var msg = incomingUpdate.message();
        var lang = data.getLang();
        var user = data.getUserId();

        if (!data.isAuthorized() || msg == null) {
            return new MaybeResponse();
        }

        if (!isWebAppInfo(msg)) {
            return new MaybeResponse();
        }

        try {
            Booking info = parser.constructFromWebapp(msg.webAppData().data(), data);

            Boolean isValidated = DateTime.isValid(info.start);
            if (isValidated == null || !isValidated) {
                return new MaybeResponse(
                        new Response(
                                data, new SendMessage(user, lang.invalidBookingTime())
                        )
                );
            }

            var response = outlook.bookRoom(info.room.id,
                    info.convertToBookRoomRequestFromWebapp());
            return new MaybeResponse(bookingHelper.processResponse(response, data));
        } catch (Exception e) {
            e.printStackTrace();
            return new MaybeResponse(bookingHelper.abnormalMenuReturn(data));
        }
    }

    /**
     * Method to check whether given command corresponds to the pushing on the WebApp button
     *
     * @param message given command
     * @return true if it is 'WebApp button' command, false - otherwise
     */
    private boolean isWebAppInfo(Message message) {
        return message.webAppData() != null;
    }
}
