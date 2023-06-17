package config;

/**
 * Interface common for all texts with different languages
 */
interface IText {
    // Bot Buttons' messages
    String BookRoomBtn();
    String CheckBookingsBtn();

    // Bot answers
    String BookRoomMsg_Answer();
    String CheckBookingsMsg_Answer();

    // Bot messages
    String NewbiesMsg();
}
