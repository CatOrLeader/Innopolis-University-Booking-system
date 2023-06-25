package dialog.handlers.state;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dialog.Keyboards;
import dialog.handlers.Response;
import dialog.handlers.StateHandler;
import dialog.userData.BotState;
import dialog.userData.UserData;

public class AuthenticationHandler extends StateHandler {
    @Override
    public Response handle(Update incomingUpdate, UserData data) {
        switch (data.getDialogState()) {
            case ENTER_MAIL -> {
                return handleEmailEntrance(incomingUpdate, data);
            }
            case CODE_AWAITING -> {
                return handleConfirmationCode(incomingUpdate, data);
            }
            default -> {
                return new Response(data);
            }
        }
    }

    private Response handleEmailEntrance(Update update, UserData data) {
        var msg = update.message();
        if (msg == null) {
            return new Response(data);
        }

        var usr = data.getUserId();
        var lang = data.getLang();
        var email = msg.text().strip();

        SendMessage botMessage;

        if (isEmailValid(email)) {
            data.setEmail(email);
            data.setDialogState(BotState.CODE_AWAITING);
            // TODO: send here code, write it somewhere (for example, in HashMap or database)
            botMessage = new SendMessage(usr, lang.verificationCodeSent());
        } else {
            botMessage = new SendMessage(usr, lang.wrongEmail());
        }
        return new Response(data, botMessage);
    }

    private Response handleConfirmationCode(Update update, UserData data) {
        var msg = update.message();
        if (msg == null) {
            return new Response(data);
        }
        var usr = data.getUserId();
        var lang = data.getLang();
        // TODO: check code
        data.setDialogState(BotState.MAIN_MENU);
        var botMessage =
                new SendMessage(usr, lang.authorized()).
                        replyMarkup(Keyboards.mainMenuMarkup(lang));
        return new Response(data, botMessage);
    }

    /**
     * Simple method to check the correct pattern of IU email
     * @param email given email
     * @return true if email has the form of IU email, false otherwise
     */
    private boolean isEmailValid(String email) {
        return email.matches("^[\\w-.]+@innopolis.university$");
    }
}
