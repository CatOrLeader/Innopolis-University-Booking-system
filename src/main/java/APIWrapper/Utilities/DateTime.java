package APIWrapper.Utilities;

import jdk.jshell.execution.LoaderDelegate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class DateTime {
    // Additional values
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.uu HH:mm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ISO_INSTANT;
    // Initial values
    private final ZonedDateTime start;
    private ZonedDateTime end;
    private Duration duration;

    public DateTime(String start, String end) {
        this.start = ZonedDateTime.of(
                LocalDateTime.from(INPUT_FORMATTER.parse(start)),
                ZoneOffset.UTC);
        this.end = ZonedDateTime.of(
                LocalDateTime.from(INPUT_FORMATTER.parse(end)),
                ZoneOffset.UTC);
        this.duration = Duration.between(this.start, this.end);
    }

    public DateTime(String start, int duration) {
        this.start = ZonedDateTime.of(
                LocalDateTime.from(INPUT_FORMATTER.parse(start)),
                ZoneOffset.UTC);
        this.duration = Duration.of(duration, ChronoUnit.MINUTES);
        this.end = this.start.plusMinutes(this.duration.toMinutes());
    }

    public DateTime(String end) {
        this.start = null;
        this.end = ZonedDateTime.of(
                LocalDateTime.from(INPUT_FORMATTER.parse(end)),
                ZoneOffset.UTC
        );
    }

    // Outer methods for parsing incoming string time to user-friendly view
    public static String formatToConvenient(String time) {
        return ZonedDateTime.parse(time).format(INPUT_FORMATTER);
    }

    public static boolean isValid(String time) {
        LocalDateTime suspect;

        try {
            suspect = LocalDateTime.parse(
                    time,
                    INPUT_FORMATTER.withResolverStyle(ResolverStyle.STRICT)
            );
        } catch (DateTimeParseException e) {
            System.out.println("DateTime log\n----incorrect format of the string: " + time + "----" +
                    "Given: " + time);
            return false;
        }

        if (LocalDateTime.now().isAfter(suspect)) {
            System.out.println("DateTime Log\n" +
                    "----Booking in the past is prohibited (*_*)----\n" +
                    "Now: " + LocalDateTime.now().format(INPUT_FORMATTER) + "\n__vs.__\nGiven: " + time);
            return false;
        }

        if (suspect.getMinute() % 15 != 0) {
            System.out.println("DateTime Log\n" +
                    "----Booking time is not satisfy the criterion (division by 15 w\\out reminder)----\n" +
                    "Given: " + time);
            return false;
        }

        return true;
    }

    // Setters
    public void setEnd(String end) {
        this.end = ZonedDateTime.of(
                LocalDateTime.from(INPUT_FORMATTER.parse(end)),
                ZoneOffset.UTC);
        duration = Duration.between(this.start, this.end);
    }

    public void setDuration(int duration) {
        this.duration = Duration.of(duration, ChronoUnit.MINUTES);
        this.end = this.start.plusMinutes(this.duration.toMinutes());
    }

    // Inner methods of the date and time formatter
    public String getOutputStart() {
        return start.format(OUTPUT_FORMATTER);
    }

    public String getOutputEnd() {
        return end.format(OUTPUT_FORMATTER);
    }
}

