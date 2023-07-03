package dialog.config;

import APIWrapper.json.Booking;
import APIWrapper.json.Room;
import APIWrapper.utilities.DateTime;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

import java.util.List;

/**
 * English bot interface localization.
 */
public class EnglishText implements IText {
    @Override
    public String initial() {
        return "Hello! To start using bot, " +
                "send me your email with the domain @innopolis.university";
    }

    @Override
    public String wrongEmail() {
        return "It seems that the provided university mail is incorrect \uD83D\uDE22 Please, send new again.";
    }

    @Override
    public String verificationCodeSent() {
        return """
                A confirmation code was sent to the specified email.
                It expires in 5 minutes. Send this code to me for authorization.\s

                You also can update email in case of any issues.""";
    }

    @Override
    public String verificationCodeWrong() {
        return "The entered code is invalid. You can enter it again or change the email.";
    }

    @Override
    public String verificationCodeExpired(String email) {
        return String.format("""
                Verification code has expired. We've sent it again to %s.

                You also can change email if something goes wrong.""", email);
    }

    @Override
    public String sorryError() {
        return "Sorry, something went wrong...";
    }

    @Override
    public String sorryEmailError() {
        return "Sorry.. Unexpected error happen. Please, input your email again.";
    }

    @Override
    public String returnToEnterEmail() {
        return "Returning back to email entering...";
    }

    @Override
    public String authorized() {
        return "You are authorized. Now you can book the rooms.";
    }

    @Override
    public String actualBookings(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            return "\uD83D\uDD12 You have no actual bookings\n";
        }
        return "\uD83D\uDD10 You have next actual bookings\n";
    }

    @Override
    public String chooseBookingTime() {
        return "Choose preferred booking start time in the format of 'DD.MM.YY HH:MM' (without quotes)";
    }

    @Override
    public String invalidBookingTime() {
        return "Input booking date or time is invalid \uD83D\uDE22 Please, try again.";
    }

    @Override
    public String chosenBookingTime(String time, String duration) {
        return String.format("Free rooms since %s during %s minutes will be looked for...", time, duration);
    }

    @Override
    public String chooseBookingDuration() {
        return "Please, choose preferred booking duration.";
    }

    @Override
    public String noAvailableRooms() {
        return "Unfortunately, there are no rooms available at this time.";
    }

    @Override
    public String hereAvailableRooms() {
        return "Here are available rooms to book at preferred period. Choose the room.";
    }

    @Override
    public String chosenRoom(String name) {
        return String.format("You've chosen %s.", name);
    }

    @Override
    public String bookingTitle() {
        return "What will be the booking title?";
    }

    @Override
    public String bookedSuccessfully(String title, String room, String since, String until) {
        return String.format(
                "Booking with title '%s' at %s, %s - %s successfully created!",
                title, room, since, until);
    }

    @Override
    public String bookedUnsuccessfully() {
        return "For certain reasons booking was unsuccessful ☹️ You may try again!";
    }

    @Override
    public String unexpectedErrorGoToMenu() {
        return "Sorry... an unexpected error has occurred. Returning to menu...";
    }

    @Override
    public String goToMenu() {
        return "Going to main menu...";
    }

    @Override
    public String bookingInterfaceClosed() {
        return "Booking interface closed.";
    }

    @Override
    public String newBookingBtn() {
        return "\uD83D\uDD0F New booking";
    }

    @Override
    public String myReservationsBtn() {
        return "\uD83D\uDD10 My bookings";
    }

    @Override
    public String changeLanguage() {
        return "\uD83C\uDDF7\uD83C\uDDFA Change language";
    }

    @Override
    public String abortAndToMenu() {
        return "Aborting all processes and going to menu...";
    }

    @Override
    public String languageChanged() {
        return "Language successfully changed.";
    }

    @Override
    public String fullBookingInfo(Booking booking) {
        return String.format("'%s' will take place at %s since %s until %s",
                booking.title,
                booking.room.name,
                DateTime.formatToConvenient(booking.start),
                DateTime.formatToConvenient(booking.end));
    }

    @Override
    public String goToBookings() {
        return "Going to your bookings...";
    }

    @Override
    public ReplyKeyboardMarkup mainMenuMarkup() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton(newBookingBtn()),
                        new KeyboardButton(myReservationsBtn())
                },
                new KeyboardButton[]{
                        new KeyboardButton(changeLanguage())
                }
        ).resizeKeyboard(true);
    }

    @Override
    public InlineKeyboardMarkup availableRoomsKeyboard(List<Room> rooms) {
        var roomButtons = rooms.stream().
                map(room ->
                        new InlineKeyboardButton[]{
                                new InlineKeyboardButton(room.name).callbackData(room.id)}).
                toArray(InlineKeyboardButton[][]::new);
        return new InlineKeyboardMarkup(roomButtons);
    }

    @Override
    public InlineKeyboardMarkup bookingDurations() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("30 min").callbackData("30"),
                        new InlineKeyboardButton("60 min").callbackData("60"),
                        new InlineKeyboardButton("90 min").callbackData("90")
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("120 min").callbackData("120"),
                        new InlineKeyboardButton("150 min").callbackData("150"),
                        new InlineKeyboardButton("180 min").callbackData("180")
                }
        );
    }

    @Override
    public InlineKeyboardMarkup userBookings(List<Booking> bookings) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        for (Booking booking : bookings) {
            markup.addRow(
                    new InlineKeyboardButton(booking.title).
                            callbackData(String.format("info %s", booking.id)),
                    new InlineKeyboardButton("Cancel ❌").
                            callbackData(String.format("cancel %s", booking.id)));
        }
        markup.addRow(new InlineKeyboardButton("◀️ Go back").callbackData("back"));
        return markup;
    }

    @Override
    public InlineKeyboardMarkup changeEmail() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton("\uD83D\uDCE8 Update email").callbackData("update")
        );
    }

    @Override
    public String upcomingBooking(Booking booking) {
        return String.format("""
                        You have an upcoming booking in 15 minutes — '%s' at %s since %s until %s.
                                        
                        You need to confirm the booking in 15 minutes, otherwise it will be cancelled.
                        """,
                booking.title,
                booking.room.name,
                DateTime.formatToConvenient(booking.start),
                DateTime.formatToConvenient(booking.end));
    }

    @Override
    public String unconfirmedBookingCancel(Booking booking) {
        return String.format("Unconfirmed booking '%s' was cancelled.", booking.title);
    }
}
