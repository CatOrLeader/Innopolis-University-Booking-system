package dialog.handlers.state;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dialog.Keyboards;
import dialog.handlers.Response;
import dialog.handlers.StateHandler;
import dialog.userData.BotState;
import dialog.userData.UserData;
import mail.AuthPair;
import mail.Client;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationHandler extends StateHandler {

    private final Client mailClient = new Client();
    private final Map<Long, AuthPair> authMap = new HashMap<>();

    public AuthenticationHandler() throws NoSuchProviderException {
    }

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

        if (isEnterpriseEmail(email)) {
            try {
                var code = generateCode();
                mailClient.sendAuthenticationCode(email, code);
                authMap.put(usr, new AuthPair(email, code));
                data.setDialogState(BotState.CODE_AWAITING);
                botMessage = new SendMessage(usr, lang.verificationCodeSent());
            } catch (MessagingException e) {
                data.setDialogState(BotState.ENTER_MAIL);
                botMessage = new SendMessage(usr, lang.wrongEmail());
            }
        } else {
            data.setDialogState(BotState.ENTER_MAIL);
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
        var inputCode = msg.text().strip();

        var realCode = authMap.get(usr).code();

        // TODO: implement keyboard to allow to change the email
        if (!realCode.equals(inputCode)) {
            var botMessage = new SendMessage(usr, lang.authenticationCodeWrong());
            return new Response(data, botMessage);
        }

        data.setEmail(authMap.get(usr).email());
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
    private boolean isEnterpriseEmail(String email) {
        return email.matches("^[\\w-.]+@innopolis.university$");
    }

    /**
     * Method to generate code between [10^6, 10^7)
     * @return code parsed to string
     */
    private String generateCode() {
        var minBound = 100000;
        var maxBound = 999999;
        return String.valueOf((int) ((Math.random() * (maxBound - minBound)) + minBound));
    }
}
