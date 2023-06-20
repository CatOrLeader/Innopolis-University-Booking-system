package config;

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
    public String verificationCodeSent() {
        return "Verification code has been sent to this email. Send me this code back.";
    }

    @Override
    public String authorized() {
        return "You are authorized. Now you can book the rooms.";
    }

    @Override
    public String newBooking() {
        return "\uD83D\uDD0F New booking";
    }

    @Override
    public String myReservations() {
        return "\uD83D\uDD10 My bookings";
    }

    @Override
    public String reservationsHere() {
        return "Here are your reservations.";
    }
}
