package Database.Controllers;

import Database.DbConnection;
import Database.Services.UserDataService;

/**
 * Controller for the user's data
 */
public class UserDataController {
    // database connetion
    private final DbConnection connection = new DbConnection();
    private final UserDataService userDataService;

    public UserDataController() {
        userDataService = new UserDataService(connection.getConnection());
    }

    /**
     * Adds a new bot user to the database
     *
     * @param tgChatId     user's chat id
     * @param email        user's email
     * @param isAuthorized email confirmation
     */
    public void addUserData(long tgChatId, String email, boolean isAuthorized) {
        userDataService.addUserData(tgChatId, email, isAuthorized);
    }

    /**
     * Updates data about telegram bot user
     *
     * @param tgChatId     user's chat id
     * @param email        user's email
     * @param isAuthorized email confirmation
     */
    public void updateUserData(long tgChatId, String email, boolean isAuthorized) {
        userDataService.updateUserData(tgChatId, email, isAuthorized);
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
}
