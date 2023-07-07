package Bot.Dialog.Config;

import Models.Booking;

import Utilities.Config;
import Utilities.DateTime;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.List;

/**
 * English bot interface localization.
 */
public class EnglishText implements IText {
    @Override
    public String wrongEmail() {
        return "It seems that the provided university mail is incorrect \uD83D\uDE22 Please, send new again.";
    }

    @Override
    public String verificationCodeSent() {
        return """
                A confirmation code was sent to the specified email.
                It expires in 5 minutes. Send this code to me for authorization.\s

                You also can input another email in case of any issues.""";
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
    public String enterEmail() {
        return "Please, enter your email with @innopolis.university domain in order to use this bot.";
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
        return """
                Choose preferred booking start time in the format of 'DD.MM.YY HH:MM' (without quotes).
                
                Booking periods are only available in 15-minute increments (0, 15, 30, 45)!
                """;
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
    public String bookingConfirmed() {
        return "Booking successfully confirmed!";
    }

    @Override
    public String bookingRevoked() {
        return "Booking successfully revoked!";
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
    public String openWebAppBtn() {
        return "New Booking (WebApp)";
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
    public InlineKeyboardMarkup bookingDurations() {
        return _localizedBookingDurations("min");
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
                new InlineKeyboardButton("\uD83D\uDCE8 Use another email").callbackData("update")
        );
    }

    @Override
    public InlineKeyboardMarkup bookingConfirmation(Booking booking) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton("✅ Confirm").callbackData(String.format("confirm %s", booking.id)),
                new InlineKeyboardButton("❌ Revoke").callbackData(String.format("revoke %s", booking.id))
        );
    }

    @Override
    public String upcomingBooking(Booking booking) {
        return String.format("""
                        You have an upcoming booking — '%s' at %s since %s until %s.
                                        
                        You need to confirm the booking in %d minutes, otherwise it will be cancelled.
                        """,
                booking.title,
                booking.room.name,
                DateTime.formatToConvenient(booking.start),
                DateTime.formatToConvenient(booking.end),
                Config.bookingReminderPeriod());
    }

    @Override
    public String unconfirmedBookingCancel(Booking booking) {
        return String.format("Unconfirmed booking '%s' was cancelled.", booking.title);
    }
}
