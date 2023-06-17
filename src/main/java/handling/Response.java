package handling;

import com.pengrad.telegrambot.request.BaseRequest;
import handling.context.BotState;

/**
 * Record describing ConcreteHandler response on corresponding update.
 * @param botResponse request that must be executed by bot in order to answer to user.
 * @param nextState state in dialog FSA that user must take.
 */
public record Response(BaseRequest botResponse, BotState nextState) {

}
