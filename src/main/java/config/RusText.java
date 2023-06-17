package config;

/**
 * All the messages were in the same language.
 * Methods are the same for all types of texts, according to the IText Interface.
 * Text Language: russian
 */
class RusText implements IText {
    // Bot Buttons' messages
    @Override
    public String BookRoomBtn() {
        return "Забронировать комнату";
    }

    @Override
    public String CheckBookingsBtn() {
        return "Посмотреть мои бронирования";
    }

    // Bot answers
    @Override
    public String BookRoomMsg_Answer() {
        return "Здесь пока нет такой возможности :(";
    }

    @Override
    public String CheckBookingsMsg_Answer() {
        return "Вы не можете забронировать комнаты --> вы не можете посмотреть свои бронирования :)";
    }

    // Bot messages
    @Override
    public String NewbiesMsg() {
        return "Привет! Мы видимся впервые, так что я постараюсь тебя запомнить!";
    }
}
