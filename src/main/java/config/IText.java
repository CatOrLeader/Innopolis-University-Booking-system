package config;

/**
 * Interface describing messages of dialog in bot.
 */
public interface IText {
    // Messages text
    String initial();
    String verificationCodeSent();
    String authorized();
    String reservationsHere();

    // Buttons text
    String newBooking();
    String myReservations();
}
