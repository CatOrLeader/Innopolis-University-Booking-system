package dialog.handlers.state;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import dialog.config.IText;
import dialog.data.BotState;
import dialog.data.UserData;
import dialog.handlers.Response;
import dialog.handlers.StateHandler;
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

    /**
     * Handle email input from user
     *
     * @param update incoming update
     * @param data   user data
     * @return bot response
     */
    private Response handleEmailEntrance(Update update, UserData data) {
        var msg = update.message();
        if (msg == null) {
            return new Response(data);
        }

        var usr = data.getUserId();
        var lang = data.getLang();
        var email = msg.text().strip();

        if (isEnterpriseEmail(email)) {
            return trySendCodeOrSorry(usr, lang, email, data, false);
        }

        data.setDialogState(BotState.ENTER_MAIL);
        var botMessage = new SendMessage(usr, lang.wrongEmail());
        return new Response(data, botMessage);
    }

    /**
     * Handle user request to finish authorization
     * by confirmation code
     *
     * @param update incoming update
     * @param data   user data
     * @return bot response
     */
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
        // If data lost during runtime fail
        if (authData == null) {
            data.setDialogState(BotState.ENTER_MAIL);
            return new Response(data, new SendMessage(usr, lang.sorryEmailError()));
        }

        var inputCode = msg.text().strip();

        if (didExpire(authData)) {
            return trySendCodeOrSorry(usr, lang, authData.email(), data, true);
        }

        if (!isCorrect(authData, inputCode)) {
            var botMessage =
                    new SendMessage(usr,
                            lang.verificationCodeWrong());
            return new Response(data, botMessage);
        }

        data.setAuthorized(true);
        data.setEmail(authData.email());
        data.setDialogState(BotState.MAIN_MENU);

        var botMessage =
                new SendMessage(usr, lang.authorized()).
                        replyMarkup(lang.mainMenuMarkup());
        return new Response(data, botMessage);
    }

    /**
     * Simple method to check the correct pattern of IU email.
     *
     * @param email given email
     * @return true if email has the form of IU email, false otherwise
     */
    private boolean isEnterpriseEmail(String email) {
        return email.matches("^[\\w-.]+@innopolis.university$");
    }

    /**
     * Method to generate code between [10^6, 10^7).
     *
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
     *
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
     *
     * @param data      user authentication data
     * @param inputCode code that user give to bot
     * @return true if codes coincide, false - otherwise
     */
    private boolean isCorrect(AuthData data, String inputCode) {
        return data.code().equals(inputCode.strip());
    }

    /**
     * Method that tries to send verification code.
     *
     * @param usr     user Telegram ID
     * @param lang    user language (IText)
     * @param email   user email (unconfirmed)
     * @param data    user data
     * @param expired flag to determine message type
     * @return bot response
     */
    private Response trySendCodeOrSorry(Long usr, IText lang, String email, UserData data, boolean expired) {
        var code = generateCode();
        try {
            mailClient.sendAuthenticationCode(email, code);
            authMap.put(usr, new AuthData(email, code, Instant.now()));
            data.setDialogState(BotState.CODE_AWAITING);
            var text = (expired ? lang.verificationCodeExpired(email) : lang.verificationCodeSent());
            var msg = new SendMessage(
                    usr,
                    text)
                    .replyMarkup(lang.changeEmail());
            return new Response(data, msg);
        } catch (MessagingException e) {
            data.setDialogState(BotState.ENTER_MAIL);
            return new Response(data, new SendMessage(usr, lang.sorryEmailError()));
        }
    }
}
