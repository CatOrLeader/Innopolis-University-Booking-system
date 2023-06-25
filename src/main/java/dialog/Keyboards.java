package dialog;

import APIWrapper.json.Room;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import config.IText;

import java.util.List;

/**
 * Class that allows to obtain keyboards that are used in bot.
 */
public class Keyboards {
    public static ReplyKeyboardMarkup mainMenuMarkup(IText lang) {
        return new ReplyKeyboardMarkup(
                new KeyboardButton(lang.newBookingBtn()), new KeyboardButton(lang.myReservationsBtn())
        ).resizeKeyboard(true);
    }

    public static InlineKeyboardMarkup availableRoomsKeyboard(List<Room> rooms) {
        var roomButtons = rooms.stream().
                map(room ->
                        new InlineKeyboardButton[]{
                                new InlineKeyboardButton(room.name).callbackData(room.id)}).
                toArray(InlineKeyboardButton[][]::new);
        return new InlineKeyboardMarkup(roomButtons);
    }

    public static InlineKeyboardMarkup bookingDurations() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[] {
                        new InlineKeyboardButton("30 min").callbackData("30"),
                        new InlineKeyboardButton("60 min").callbackData("60"),
                        new InlineKeyboardButton("90 min").callbackData("90")
                },
                new InlineKeyboardButton[] {
                        new InlineKeyboardButton("120 min").callbackData("120"),
                        new InlineKeyboardButton("150 min").callbackData("150"),
                        new InlineKeyboardButton("180 min").callbackData("180")
                }
        );
    }
}
