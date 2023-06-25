package Models;

import dialog.userData.BotState;

/**
 * Model for the User data access in database
 */
public class UserDataModel {
    public final long userId;
    public String email;
    public boolean isAuthorized;
    public BotState dialogState;
    public String language;

    public UserDataModel(long userId, String email, boolean isAuthorized, BotState dialogState,  String language) {
        this.userId = userId;
        this.dialogState = dialogState;
        this.email = email;
        this.language = language;
        this.isAuthorized = isAuthorized;
    }
}
