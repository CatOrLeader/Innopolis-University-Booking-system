package handling;

import com.pengrad.telegrambot.request.BaseRequest;
import handling.context.BotState;

/**
 * Record describing ConcreteHandler response on corresponding update.
 * @param nextState state in dialog FSA that user must take.
 * @param botResponse requests that must be executed by bot in order to answer to user.
 */
public record Response(BotState nextState, BaseRequest... botResponse) {

}
