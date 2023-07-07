package Bot.Dialog.Config;

import Models.Booking;
import Utilities.Config;
import Utilities.DateTime;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.List;

/**
 * Russian bot interface localization.
 */
public class RussianText implements IText {
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
        return """
                Выберите предпочтительное время начала брони в формате 'ДД.ММ.ГГ ЧЧ:ММ' (без кавычек)."
                
                Доступны периоды бронирования только с шагом 15 минут (0, 15, 30, 45)!
                """;
    }

    @Override
    public String invalidBookingTime() {
        return "Введеная дата или время некорректны \uD83D\uDE22 Пожалуйста, повторите попытку.";
    }

    @Override
    public String chosenBookingTime(String time, String duration) {
        return String.format("Ищу комнаты, свободные с %s в течение %s минут...", time, duration);
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
    public String abortAndToMenu() {
        return "Прерываю все процессы и перехожу в меню...";
    }

    @Override
    public String languageChangedAndToMenu() {
        return "Язык успешно изменен. Перехожу в меню.";
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
    public InlineKeyboardMarkup bookingDurations() {
        return _localizedBookingDurations("минут");
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
                        У Вас есть предстоящее бронирование — '%s' в %s с %s до %s.
                                        
                        Необходимо подтвердить бронирование в течение %d минут, иначе оно будет отменено.""",
                booking.title,
                booking.room.name,
                DateTime.formatToConvenient(booking.start),
                DateTime.formatToConvenient(booking.end),
                Config.bookingReminderPeriod());
    }

    @Override
    public String unconfirmedBookingCancel(Booking booking) {
        return String.format("Неподтвержденное бронирование '%s' было отменено.", booking.title);
    }
}
