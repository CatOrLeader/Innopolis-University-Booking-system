package dialog.data;

import Models.UserDataModel;
import dialog.config.EnglishText;
import dialog.config.IText;

/**
 * Class to store external user information.
 * Will be obtained from database.
 */
public class UserData {
    private final long userId;
    private BotState dialogState;
    private String email;
    private IText lang;
    private boolean isAuthorized = false;

    public UserData(long userId, BotState dialogState, String email, IText lang) {
        this.userId = userId;
        this.dialogState = dialogState;
        this.email = email;
        this.lang = lang;
    }

    public UserData(long userId, BotState dialogState, String email, IText lang, boolean isAuthorized) {
        this.userId = userId;
        this.dialogState = dialogState;
        this.email = email;
        this.lang = lang;
        this.isAuthorized = isAuthorized;
    }


    public long getUserId() {
        return userId;
    }

    public BotState getDialogState() {
        return dialogState;
    }

    public void setDialogState(BotState dialogState) {
        this.dialogState = dialogState;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public IText getLang() {
        return lang;
    }

    public void setLang(IText lang) {
        this.lang = lang;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }

    public UserDataModel toUserDataModel() {
        var language = (lang instanceof EnglishText ? "ENG" : "RUS");
        return new UserDataModel(userId, email, isAuthorized, dialogState, language);
    }
}
