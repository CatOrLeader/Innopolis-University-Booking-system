package Bot.Dialog.Config;

import APIWrapper.Json.Booking;
import APIWrapper.Json.Room;
import APIWrapper.Utilities.DateTime;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

import java.util.List;

/**
 * Russian bot interface localization.
 */
public class RussianText implements IText {
    @Override
    public String initial() {
        return """
                Hello! Let's start by choosing a language we feel comfortable speaking.
                
                Привет! Давай для начала выберем язык, на котором будет удобно общаться.
                """;
    }

    @Override
    public String wrongEmail() {
        return "Кажется, что предоставленная университетская почта некорректна \uD83D\uDE22 Пожалуйста, отправьте другую еще раз.";
    }

    @Override
    public String verificationCodeSent() {
        return """
                На указанный адрес электронной почты был отправлен код подтверждения.
                Его срок действия истекает через 5 минут. Пришлите мне этот код для авторизации.\s

                Вы также можете обновить адрес электронной почты в случае возникновения каких-либо проблем.""";
    }

    @Override
    public String verificationCodeWrong() {
        return "Введенный код некорректен. Вы можете ввести код снова или изменить адрес электронной почты.";
    }

    @Override
    public String verificationCodeExpired(String email) {
        return String.format("""
                Срок действия кода подтверждения истек. Мы снова отправили его на %s.

                Вы также можете изменить адрес электронной почты, если что-то пойдет не так.""", email);
    }

    @Override
    public String sorryError() {
        return "Извините, что-то пошло не так...";
    }

    @Override
    public String sorryEmailError() {
        return "Извините... Произошла непредвиденная ошибка. Пожалуйста, введите свой адрес электронной почты еще раз.";
    }

    @Override
    public String enterEmail() {
        return "Введите, пожалуйста, адрес электронной почты " +
                "с доменом @innopolis.university чтобы использовать данного бота.";
    }

    @Override
    public String authorized() {
        return "Вы авторизованы. Теперь Вы можете забронировать комнату.";
    }

    @Override
    public String actualBookings(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            return "\uD83D\uDD12 У Вас нет актуальных бронирований\n";
        }
        return "\uD83D\uDD10 У Вас есть следующие актуальные бронирования\n";
    }

    @Override
    public String chooseBookingTime() {
        return "Выберите предпочтительное время начала брони в формате 'ДД.ММ.ГГ ЧЧ:ММ' (без кавычек)";
    }

    @Override
    public String invalidBookingTime() {
        return "Введеная дата или время некорректны \uD83D\uDE22 Пожалуйста, повторите попытку.";
    }

    @Override
    public String chosenBookingTime(String time, String duration) {
        return String.format("Ищу комнаты, свободные с %s в течении %s минут...", time, duration);
    }

    @Override
    public String chooseBookingDuration() {
        return "Пожалуйста, выберите предпочтительную длительность брони.";
    }

    @Override
    public String noAvailableRooms() {
        return "К сожалению, в заданное время свободных комнат нет.";
    }

    @Override
    public String hereAvailableRooms() {
        return "Список свободных комнат, которые можно забронировать на этот период. Выберите комнату.";
    }

    @Override
    public String chosenRoom(String name) {
        return String.format("Вы выбрали %s.", name);
    }

    @Override
    public String bookingTitle() {
        return "Напишите название своей брони";
    }

    @Override
    public String bookedSuccessfully(String title, String room, String since, String until) {
        return String.format(
                "Бронирование '%s' в %s, %s - %s успешно создано!",
                title, room, since, until);
    }

    @Override
    public String bookedUnsuccessfully() {
        return "По некоторым причинам бронирование не удалось ☹️ Вы можете попробовать снова!";
    }

    @Override
    public String unexpectedErrorGoToMenu() {
        return "Прошу прощения... произошла непредвиденная ошибка. Возврат к меню...";
    }

    @Override
    public String goToMenu() {
        return "Переход в главное меню...";
    }

    @Override
    public String bookingInterfaceClosed() {
        return "Интерфейс бронирования закрыт.";
    }

    @Override
    public String bookingConfirmed() {
        return "Бронирование успешно подтверждено!";
    }

    @Override
    public String bookingRevoked() {
        return "Бронирование успешно отменено";
    }

    @Override
    public String newBookingBtn() {
        return "\uD83D\uDD0F Новая бронь";
    }

    @Override
    public String myReservationsBtn() {
        return "\uD83D\uDD10 Мои брони";
    }

    @Override
    public String changeLanguage() {
        return "🇬🇧 Сменить язык";
    }

    @Override
    public String abortAndToMenu() {
        return "Прерываю все процессы и перехожу в меню...";
    }

    @Override
    public String languageChanged() {
        return "Язык успешно изменен.";
    }

    @Override
    public InlineKeyboardMarkup languageSelection() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton("\uD83C\uDDF7\uD83C\uDDFA Russian").
                        callbackData("language rus"),
                new InlineKeyboardButton("\uD83C\uDDEC\uD83C\uDDE7 English").
                        callbackData("language eng")
        );
    }

    @Override
    public String fullBookingInfo(Booking booking) {
        return String.format("'%s' будет проведено в %s с %s до %s",
                booking.title,
                booking.room.name,
                DateTime.formatToConvenient(booking.start),
                DateTime.formatToConvenient(booking.end));
    }

    @Override
    public String goToBookings() {
        return "Перехожу к Вашим броням...";
    }

    @Override
    public ReplyKeyboardMarkup mainMenuMarkup() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton(newBookingBtn()),
                        new KeyboardButton(myReservationsBtn())
                },
                new KeyboardButton[]{
                        new KeyboardButton(changeLanguage())
                }
        ).resizeKeyboard(true);
    }

    @Override
    public InlineKeyboardMarkup availableRoomsKeyboard(List<Room> rooms) {
        var roomButtons = rooms.stream().
                map(room ->
                        new InlineKeyboardButton[]{
                                new InlineKeyboardButton(room.name).callbackData(room.id)}).
                toArray(InlineKeyboardButton[][]::new);
        return new InlineKeyboardMarkup(roomButtons);
    }

    @Override
    public InlineKeyboardMarkup bookingDurations() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("30 минут").callbackData("30"),
                        new InlineKeyboardButton("60 минут").callbackData("60"),
                        new InlineKeyboardButton("90 минут").callbackData("90")
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("120 минут").callbackData("120"),
                        new InlineKeyboardButton("150 минут").callbackData("150"),
                        new InlineKeyboardButton("180 минут").callbackData("180")
                }
        );
    }

    @Override
    public InlineKeyboardMarkup userBookings(List<Booking> bookings) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        for (Booking booking : bookings) {
            markup.addRow(
                    new InlineKeyboardButton(booking.title).
                            callbackData(String.format("info %s", booking.id)),
                    new InlineKeyboardButton("Отменить ❌").
                            callbackData(String.format("cancel %s", booking.id)));
        }
        markup.addRow(new InlineKeyboardButton("◀️ Назад").callbackData("back"));
        return markup;
    }

    @Override
    public InlineKeyboardMarkup changeEmail() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton("\uD83D\uDCE8 Ввести другую почту").callbackData("update")
        );
    }

    @Override
    public InlineKeyboardMarkup bookingConfirmation(Booking booking) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton("✅ Подтвердить").callbackData(String.format("confirm %s", booking.id)),
                new InlineKeyboardButton("❌ Отменить").callbackData(String.format("revoke %s", booking.id))
        );
    }

    @Override
    public String upcomingBooking(Booking booking) {
        return String.format("""
                        У Вас есть бронирование через 15 минут — '%s' в %s с %s до %s.
                                        
                        Необходимо подтвердить бронирование в течение 15 минут, иначе оно будет отменено.""",
                booking.title,
                booking.room.name,
                DateTime.formatToConvenient(booking.start),
                DateTime.formatToConvenient(booking.end));
    }

    @Override
    public String unconfirmedBookingCancel(Booking booking) {
        return String.format("Неподтвержденное бронирование '%s' было отменено.", booking.title);
    }
}
