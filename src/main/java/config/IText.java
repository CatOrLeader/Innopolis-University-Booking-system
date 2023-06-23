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
    String chosenBookingTime(String time, String duration);
    String chooseBookingDuration();
    String bookingInterrupted();
    String noAvailableRooms();
    String hereAvailableRooms();
    String chosenRoom(String name);
    String bookingTitle();
    String bookedSuccessfully();
    String bookedUnsuccessfully();

    // Buttons text
    String newBookingBtn();
    String myReservationsBtn();

    // Info messages
    String printReservation(String name, String room, String since, String until);

}
