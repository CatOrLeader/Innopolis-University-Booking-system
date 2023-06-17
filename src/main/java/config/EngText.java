package config;

/**
 * All the messages were in the same language.
 * Methods are the same for all types of texts, according to the IText Interface.
 * Text Language: english
 */
class EngText implements IText {
    // Bot Buttons' messages
    @Override
    public String BookRoomBtn() {
        return "Book a room";
    }

    @Override
    public String CheckBookingsBtn() {
        return "Check my bookings";
    }

    // Bot answers
    @Override
    public String BookRoomMsg_Answer() {
        return "There is no such opportunity yet :(";
    }

    @Override
    public String CheckBookingsMsg_Answer() {
        return "You can not book rooms -> You can not check your reservations :)";
    }

    // Bot messages
    @Override
    public String NewbiesMsg() {
        return "Hello! I see you first time and will try to remember!";
    }
}
