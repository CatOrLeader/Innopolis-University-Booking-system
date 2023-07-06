package Bot.Dialog.Data;

/**
 * Enum describing states of the bot-user dialog FSM.
 */
public enum BotState {
    UNINITIALIZED,
    ENTER_MAIL,
    CODE_AWAITING,
    MAIN_MENU,
    LIST_OF_RESERVATIONS,
    BOOKING_TIME_AWAITING,
    BOOKING_DURATION_AWAITING,
    ROOM_AWAITING,
    BOOKING_TITLE_AWAITING,
    INITIAL_LANGUAGE_SETTING
    // TODO: Extend the list fully
}
