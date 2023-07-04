package Models;

import dialog.config.EnglishText;
import dialog.config.RussianText;
import dialog.data.BotState;
import dialog.data.UserData;

/**
 * Model for the User data access in database
 */
public class UserDataModel {
    public final long userId; // user's chat id
    public String email; // user's email
    public boolean isAuthorized; // is user authorized in bot via email or not
    public BotState dialogState; // current state of the chat with bot
    public String language; // ENG, RUS

    public UserDataModel(long userId, String email, boolean isAuthorized, BotState dialogState, String language) {
        this.userId = userId;
        this.dialogState = dialogState;
        this.email = email;
        this.language = language;
        this.isAuthorized = isAuthorized;
    }

    public UserData toUserData() {
        var lang = (language.equals("ENG") ? new EnglishText() : new RussianText());
        return new UserData(userId, dialogState, email, lang, isAuthorized);
    }
}
