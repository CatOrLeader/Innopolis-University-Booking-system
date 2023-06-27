package dialog.handlers.state;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import dialog.handlers.Response;
import dialog.handlers.StateHandler;
import dialog.userData.BotState;
import dialog.userData.UserData;
import mail.AuthData;
import mail.Client;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationHandler extends StateHandler {

    private final Client mailClient = new Client();
    private final Map<Long, AuthData> authMap = new HashMap<>();

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
                authMap.put(usr, new AuthData(email, code, Instant.now()));
                data.setDialogState(BotState.CODE_AWAITING);
                botMessage = new SendMessage(
                        usr,
                        lang.verificationCodeSent())
                        .replyMarkup(lang.changeEmail());
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
        var query = update.callbackQuery();

        var usr = data.getUserId();
        var lang = data.getLang();

        if (msg == null && query == null) {
            return new Response(data);
        }

        if (query != null && query.data().equals("update")) {
            data.setDialogState(BotState.ENTER_MAIL);
            var updateMessage =
                    new EditMessageText(
                            usr,
                            query.message().messageId(),
                            lang.returnToEnterEmail()
                    );
            return new Response(data, updateMessage);
        } else if (query != null) {
            return new Response(data);
        }

        var authData = authMap.get(usr);
        var inputCode = msg.text().strip();

        if (didExpire(authData)) {
            try {
                var newCode = generateCode();
                authMap.put(usr, new AuthData(authData.email(), newCode, Instant.now()));
                mailClient.sendAuthenticationCode(authData.email(), newCode);
                var botMessage = new SendMessage(
                        usr,
                        lang.verificationCodeExpired(authData.email())
                ).replyMarkup(lang.changeEmail());
                return new Response(data, botMessage);
            } catch (MessagingException e) {
                // TODO: may be say about error and re-enter email
            }
        }

        if (!isCorrect(authData, inputCode)) {
            var botMessage =
                    new SendMessage(usr,
                            lang.verificationCodeWrong());
            return new Response(data, botMessage);
        }

        data.setEmail(authData.email());
        data.setDialogState(BotState.MAIN_MENU);

        var botMessage =
                new SendMessage(usr, lang.authorized()).
                        replyMarkup(lang.mainMenuMarkup());
        return new Response(data, botMessage);
    }

    /**
     * Simple method to check the correct pattern of IU email.
     * @param email given email
     * @return true if email has the form of IU email, false otherwise
     */
    private boolean isEnterpriseEmail(String email) {
        return email.matches("^[\\w-.]+@innopolis.university$");
    }

    /**
     * Method to generate code between [10^6, 10^7).
     * @return code parsed to string
     */
    private String generateCode() {
        // Code bounds
        var minBound = 100000;
        var maxBound = 999999;
        return String.valueOf((int) ((Math.random() * (maxBound - minBound)) + minBound));
    }

    /**
     * Method to check whether generated code expired.
     * @param data user authentication data
     * @return true if code expired, false - otherwise
     */
    private boolean didExpire(AuthData data) {
        var elapsedTime = Duration.between(data.birthTime(), Instant.now()).toMinutes();
        var expirationTime = 5;
        return elapsedTime >= expirationTime;
    }

    /**
     * Method to check whether user input code coincides with generated one.
     * @param data user authentication data
     * @param inputCode code that user give to bot
     * @return true if codes coincide, false - otherwise
     */
    private boolean isCorrect(AuthData data, String inputCode) {
        return data.code().equals(inputCode.strip());
    }
}
