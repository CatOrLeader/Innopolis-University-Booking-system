package handling.context;

// TODO: Authentication automaton

/**
 * Enum describing states of the bot-user dialog FSM.
 */
public enum BotState {
    NO_STATE,
    MAIN_MENU_STATE,
    VIEW_ALL_BOOKINGS,
    CONFIRM_BOOKING_CANCELLATION,
    CREATE_BOOKING
}
