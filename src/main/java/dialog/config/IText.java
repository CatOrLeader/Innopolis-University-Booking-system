package dialog.config;

import APIWrapper.json.Booking;
import APIWrapper.json.Room;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

import java.util.List;

/**
 * Interface describing messages of dialog in bot.
 */
public interface IText {
    // Messages text
    String initial();

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

    // Pop-ups text
    String fullBookingInfo(Booking booking);

    // Transitions
    String returnToEnterEmail();

    String goToBookings();

    String abortAndToMenu();

    String languageChanged();

    // Keyboards
    ReplyKeyboardMarkup mainMenuMarkup();

    InlineKeyboardMarkup availableRoomsKeyboard(List<Room> rooms);

    InlineKeyboardMarkup bookingDurations();

    InlineKeyboardMarkup userBookings(List<Booking> bookings);

    InlineKeyboardMarkup changeEmail();

    InlineKeyboardMarkup bookingConfirmation(Booking booking);

    // Notifications
    String upcomingBooking(Booking booking);

    String unconfirmedBookingCancel(Booking booking);
}
