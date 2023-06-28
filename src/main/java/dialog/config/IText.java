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
    String sorryEmailError();
    String returnToEnterEmail();
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
    String goToMenu();
    String bookingInterfaceClosed();

    // Buttons text
    String newBookingBtn();
    String myReservationsBtn();

    // Info messages
    String printReservation(String name, String room, String since, String until);

    // Independent handlers' messages
    String abortAndToMenu();

    // Pop-ups text
    String fullBookingInfo(Booking booking);

    String goToBookings();

    // Keyboards
    ReplyKeyboardMarkup mainMenuMarkup();
    InlineKeyboardMarkup availableRoomsKeyboard(List<Room> rooms);
    InlineKeyboardMarkup bookingDurations();
    InlineKeyboardMarkup userBookings(List<Booking> bookings);
    InlineKeyboardMarkup changeEmail();
}
