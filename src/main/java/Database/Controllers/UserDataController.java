package Database.Controllers;

import Bot.Dialog.Config.EnglishText;
import Bot.Dialog.Data.BotState;
import Bot.Dialog.Data.UserData;
import Database.DbConnector;
import Database.Services.UserDataService;

/**
 * Controller for the user's data
 */
public class UserDataController {
    // database connection
    private final DbConnector connection = new DbConnector();
    private final UserDataService userDataService;

    public UserDataController() {
        userDataService = new UserDataService(connection.getConnection());
    }

    /**
     * Method to set new user data for given Telegram ID.
     *
     * @param userId ID of user whose data will be set.
     * @param data   new data.
     */
    public void setUserData(long userId, UserData data) {
        if (userExists(userId)) {
            userDataService.updateUserData(data);
        } else {
            userDataService.addUserData(data);
        }
    }

    /**
     * Get user's data by ID
     *
     * @param userId user's chat id
     * @return model of the user
     */
    public UserData getUserData(long userId) {
        if (!userExists(userId)) {
            initializeUser(userId);
        }
        return userDataService.getUserData(userId);
    }

    /**
     * Checks if user exists in database
     *
     * @param tgChatId user's chat id
     * @return true if user exists, false otherwise
     */
    public boolean userExists(long tgChatId) {
        return userDataService.userExists(tgChatId);
    }

    /**
     * Checks if user authorized
     *
     * @param tgChatId user's chat id
     * @return true if user exists, false otherwise
     */
    public boolean isAuthorized(long tgChatId) {
        return userDataService.isAuthorized(tgChatId);
    }

    /**
     * Method to set initial data for new user.
     *
     * @param user new user
     */
    private void initializeUser(long user) {
        var initialData = new UserData(
                user,
                BotState.UNINITIALIZED,
                null,
                new EnglishText());
        setUserData(user, initialData);
    }
}
