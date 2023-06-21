package handling.userData;

/**
 * Enum describing states of the bot-user dialog FSM.
 */
public enum BotState {
    UNINITIALIZED,
    ENTER_MAIL,
    CODE_AWAITING,
    MAIN_MENU,
    LIST_OF_RESERVATIONS,
    CANCEL_CONFIRMATION,
    BOOKING_TIME_AWAITING,
    // TODO: Extend the list fully
}
