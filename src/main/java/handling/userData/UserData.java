package handling.userData;

import config.IText;

/**
 * Class to store external user information.
 * Will be obtained from database.
 * TODO: probably add mail status (boolean)
 */
public class UserData {
    private String userId;
    private BotState dialogState;
    private String email;
    private IText lang;

    public UserData(String userId, BotState dialogState, String email, IText lang) {
        this.userId = userId;
        this.dialogState = dialogState;
        this.email = email;
        this.lang = lang;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}