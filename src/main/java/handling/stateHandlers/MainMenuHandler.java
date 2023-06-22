package handling.stateHandlers;

import APIWrapper.json.Booking;
import APIWrapper.requests.Request;
import APIWrapper.utilities.DateTime;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import config.IText;
import handling.Response;
import handling.StateHandler;
import handling.userData.BotState;
import handling.userData.UserData;

import java.util.List;

public class MainMenuHandler extends StateHandler {
    private final Request outlook = new Request("http://localhost:3000");
    private final String[] rooms =
            outlook.getAllBookableRooms().stream().map(room -> room.id).toArray(String[]::new);
    @Override
    public Response handle(Update incomingUpdate, UserData data) {
        var message = incomingUpdate.message();
        if (message == null) {
            return new Response(data);
        } else if (message.text().equals(data.getLang().myReservationsBtn())) {
            return handleReservations(data);
        } else if (message.text().equals(data.getLang().newBookingBtn())) {
            return handleNewBooking(data);
        } else {
            return new Response(data);
        }
    }

    private Response handleReservations(UserData data) {
        var lang = data.getLang();
        var bookings = outlook.getBookingsByUser(data.getEmail());
        var userReservations = bookingsMessageText(bookings, lang);
        return new Response(data, new SendMessage(data.getUserId(), userReservations));
    }

    private Response handleNewBooking(UserData data) {
        var botMessage = new SendMessage(
                data.getUserId(), data.getLang().chooseBookingTime())
                .replyMarkup(new ReplyKeyboardRemove());
        data.setDialogState(BotState.BOOKING_TIME_AWAITING);
        return new Response(data, botMessage);
    }

    private String bookingsMessageText(List<Booking> bookings, IText lang) {
        StringBuilder text;
        if (bookings == null || bookings.isEmpty()) {
            text = new StringBuilder(lang.noActualBookings());
        } else {
            text = new StringBuilder(lang.hereActualBookings());
            for (Booking booking : bookings) {
                var bookingInfo = String.format("%s — %s, %s — %s\n", booking.title,
                        booking.room.name,
                        DateTime.formatToConvenient(booking.start),
                        DateTime.formatToConvenient(booking.end));
                text.append("\n").append(bookingInfo);
            }
        }
        return text.toString();
    }
}
