package Database.Controllers;

import Database.DbConnector;
import Models.UserDataModel;
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
     * Adds a new bot user to the database
     * @param userDataModel model of user's data
     */
    public void addUserData(UserDataModel userDataModel) {
        userDataService.addUserData(userDataModel);
    }

    /**
     * Updates data about telegram bot user
     * @param userDataModel model of user's data
     */
    public void updateUserData(UserDataModel userDataModel) {
        userDataService.updateUserData(userDataModel);
    }

    /**
     * Updates user's data
     * @param tgChatId user's chat id
     * @return model of the user
     */
    public UserDataModel getUserData(long tgChatId) {
        return userDataService.getUserData(tgChatId);
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
