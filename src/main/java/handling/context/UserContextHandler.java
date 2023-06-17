package handling.context;

import java.util.HashMap;
import java.util.Map;

// TODO: Make it to work with database instead of Map.
//  The interface MUST be the same.

/**
 * Class that allows to control current state of user-bot
 * dialog for certain user.
 */
public class UserContextHandler {
    private final Map<String, BotState> userState;

    public UserContextHandler() {
        userState = new HashMap<>();
    }

    /**
     * Method to get current state of dialog for user with given id.
     * @param userId Telegram id of user.
     * @return state of the dialog for given user.
     */
    public BotState getUserState(String userId) {
        userState.putIfAbsent(userId, BotState.NO_STATE);
        return userState.get(userId);
    }

    /**
     * Method to update current state of dialog for given user.
     * @param userId Telegram id of user.
     * @param state new state of the dialog for user.
     */
    public void setUserState(String userId, BotState state) {
        userState.put(userId, state);
    }
}
