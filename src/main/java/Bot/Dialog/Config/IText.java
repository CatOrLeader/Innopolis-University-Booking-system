package Bot.Dialog.Config;

import Models.Room;
import Models.Booking;
import Utilities.Config;
import com.pengrad.telegrambot.model.WebAppInfo;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface describing messages of dialog in bot.
 */
public interface IText {
    // Messages text
    default String initial() {
        return """
                Hello! Let's start by choosing a language we feel comfortable speaking.
                
                Привет! Давай для начала выберем язык, на котором будет удобно общаться.
                """;
    }

    String wrongEmail();

    String verificationCodeSent();

    String verificationCodeWrong();

    String verificationCodeExpired(String email);

    String sorryError();

    String sorryEmailError();

    String authorized();

    String actualBookings(List<Booking> bookings);

    String chooseBookingTime();

    String invalidBookingTime();

    String chosenBookingTime(String time, String duration);

    String chooseBookingDuration();

    String noAvailableRooms();

    String hereAvailableRooms();

    String chosenRoom(String name);

    String bookingTitle();

    String bookedSuccessfully(String title, String room, String since, String until);

    String bookedUnsuccessfully();

    String unexpectedErrorGoToMenu();

    String goToMenu();

    String bookingInterfaceClosed();

    String bookingConfirmed();
    String bookingRevoked();

    // Buttons text
    String newBookingBtn();

    String myReservationsBtn();

    String changeLanguage();

    String openWebAppBtn();

    // Pop-ups text
    String fullBookingInfo(Booking booking);

    // Transitions
    String enterEmail();

    String goToBookings();

    String abortAndToMenu();

    String languageChanged();

    // Keyboards
    default InlineKeyboardMarkup languageSelection() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton("\uD83C\uDDF7\uD83C\uDDFA Russian").
                        callbackData("language rus"),
                new InlineKeyboardButton("\uD83C\uDDEC\uD83C\uDDE7 English").
                        callbackData("language eng")
        );
    }
    default ReplyKeyboardMarkup mainMenuMarkup() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton(newBookingBtn()),
                        new KeyboardButton(myReservationsBtn())
                },
                new KeyboardButton[]{
                        new KeyboardButton(changeLanguage())
                },
                new KeyboardButton[] {
                        new KeyboardButton(openWebAppBtn())
                                .webAppInfo(new WebAppInfo("https://mgregarious-beijinho-155625.netlify.app/"))
                }
        ).resizeKeyboard(true);
    }

    default InlineKeyboardMarkup availableRoomsKeyboard(List<Room> rooms) {
        var roomButtons = rooms.stream().
                map(room ->
                        new InlineKeyboardButton[]{
                                new InlineKeyboardButton(room.name).callbackData(room.id)}).
                toArray(InlineKeyboardButton[][]::new);
        return new InlineKeyboardMarkup(roomButtons);
    }

    InlineKeyboardMarkup bookingDurations();

    default InlineKeyboardMarkup _localizedBookingDurations(String sentence) {
        var durations = Config.bookingDurations();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        for (int ind = 0; ind < durations.size(); ind++) {
            var row = (ind < durations.size() / 2 ? firstRow : secondRow);
            var duration = durations.get(ind);
            row.add(
                    new InlineKeyboardButton(
                            String.format("%d %s", duration, sentence)
                    ).callbackData(duration.toString()));
        }
        return new InlineKeyboardMarkup(
                firstRow.toArray(InlineKeyboardButton[]::new),
                secondRow.toArray(InlineKeyboardButton[]::new));
    }

    InlineKeyboardMarkup userBookings(List<Booking> bookings);

    InlineKeyboardMarkup changeEmail();

    InlineKeyboardMarkup bookingConfirmation(Booking booking);

    // Notifications
    String upcomingBooking(Booking booking);

    String unconfirmedBookingCancel(Booking booking);
}
