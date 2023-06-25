package dialog.userData;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that allows to work with external user data by his
 * Telegram ID. Data can be placed in HashMap or in DB
 * TODO: add work with database
 */
public class UserDataManager {
    private final Map<Long, UserData> userData;
    public UserDataManager() {
        userData = new HashMap<>();
    }

    /**
     * Method to set new user data for given Telegram ID.
     * @param userId ID of user whose data will be set.
     * @param data new data.
     */
    public void setUserData(long userId, UserData data) {
        userData.put(userId, data);
    }

    /**
     * Method to get actual data of user with given ID.
     * @param userId ID of desired user.
     * @return user's data.
     */
    public UserData getUserData(long userId) {
        return userData.get(userId);
    }

    /**
     * Method to check whether database has data about
     * user with given ID.
     * @param userId ID of user to check existence of his data.
     * @return true if database has data of given user, false - otherwise.
     */
    public boolean hasUserData(long userId) {
        return userData.containsKey(userId);
    }
}
