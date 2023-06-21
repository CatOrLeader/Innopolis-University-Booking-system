package handling.stateHandlers;

import APIWrapper.json.Booking;
import APIWrapper.json.BookingsFilter;
import APIWrapper.json.QueryBookingsRequest;
import APIWrapper.requests.Request;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import handling.Response;
import handling.StateHandler;
import handling.userData.BotState;
import handling.userData.UserData;


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
        var bookings = outlook.queryBookings(
                new QueryBookingsRequest(new BookingsFilter(
                        "any_time_start",
                        "any_time_end",
                        rooms,
                        new String[]{data.getEmail()}
                ))
        );
        StringBuilder answer;
        if (bookings.isEmpty()) {
            answer = new StringBuilder(lang.noActualBookings());
        } else {
            answer = new StringBuilder(lang.hereActualBookings());
        }
        for (Booking booking : bookings) {
            var bookingInfo = String.format("%s — %s, %s — %s\n", booking.title,
                    booking.room.name, booking.start, booking.end);
            answer.append("\n").append(bookingInfo);
        }
        return new Response(data, new SendMessage(data.getUserId(), answer.toString()));
    }

    private Response handleNewBooking(UserData data) {
        var botMessage = new SendMessage(data.getUserId(), data.getLang().chooseBookingTime());
        data.setDialogState(BotState.BOOKING_TIME_AWAITING);
        return new Response(data, botMessage);
    }
}
