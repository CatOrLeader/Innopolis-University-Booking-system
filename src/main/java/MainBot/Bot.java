package MainBot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

public class Bot {
    private TelegramBot bot = new TelegramBot("6065382468:AAEWy7pzKrLu2teez_jtEyY_pXzQGWP5AsI");

    public void serve() {
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void process(Update update) {
        Message message = update.message();

        BaseRequest request = null;

        if (message != null){
            long chatId = update.message().chat().id();
            request = new SendMessage(chatId, "Hello! I am IU booking bot and I am not ready yet. Let my team to add functions for me!");
        }

        if (request != null){
            bot.execute(request);
        }
    }
}
