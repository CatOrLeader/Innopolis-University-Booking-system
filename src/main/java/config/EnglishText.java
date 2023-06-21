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
    public String noActualBookings() {
        return "\uD83D\uDD12 You have no actual bookings\n";
    }

    @Override
    public String hereActualBookings() {
        return "\uD83D\uDD10 You have next actual bookings\n";
    }

    @Override
    public String chooseBookingTime() {
        return "Choose preferred booking start time in the format of 'DD.MM.YY HH:MM' (without quotes)";
    }

    @Override
    public String newBookingBtn() {
        return "\uD83D\uDD0F New booking";
    }

    @Override
    public String myReservationsBtn() {
        return "\uD83D\uDD10 My bookings";
    }


}
