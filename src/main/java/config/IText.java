package config;

/**
 * Interface describing messages of dialog in bot.
 */
public interface IText {
    // Messages text
    String initial();
    String verificationCodeSent();
    String authorized();
    String noActualBookings();
    String hereActualBookings();
    String chooseBookingTime();
    String chooseBookingDuration();
    String bookingInterrupted();
    String noAvailableRooms();
    String hereAvailableRooms();

    // Buttons text
    String newBookingBtn();
    String myReservationsBtn();

}
