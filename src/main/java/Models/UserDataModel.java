package Models;

import dialog.config.EnglishText;
import dialog.userData.BotState;
import dialog.userData.UserData;

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

    public UserData toUserData() {
        // TODO: handle language here
        var lang = (language.equals("ENG") ? new EnglishText() : new EnglishText());
        return new UserData(userId, dialogState, email, lang, isAuthorized);
    }
}
