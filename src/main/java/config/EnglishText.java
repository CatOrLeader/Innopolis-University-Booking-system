package config;

import APIWrapper.json.Booking;
import APIWrapper.utilities.DateTime;

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
        return "It seems to be that provided university email is incorrect \uD83D\uDE22 Please, send new again.";
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
    public String invalidBookingTime() {
        return "Input booking time is invalid \uD83D\uDE22 Please, try again.";
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
        return "For certain reasons booking was unsuccessful :( You may try again!";
    }

    @Override
    public String goToMenu() {
        return "Going to main menu..";
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
    public String printReservation(String name, String room, String since, String until) {
        return String.format(
                "%s — at %s since %s until %s", name, room, since, until
        );
    }

    @Override
    public String abortAndToMenu() {
        return "Aborting all processes and going to menu.";
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
}
