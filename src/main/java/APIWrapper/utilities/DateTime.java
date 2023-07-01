package APIWrapper.utilities;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DateTime {
    // Initial values
    private final ZonedDateTime start;
    private ZonedDateTime end;
    private Duration duration;

    // Additional values
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ISO_INSTANT;

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

    // Outer methods for parsing incoming string time to user-friendly view
    public static String formatToConvenient(String time) {
        return ZonedDateTime.parse(time).format(INPUT_FORMATTER);
    }

    public static boolean isValid(String time) {
        try {
            INPUT_FORMATTER.parse(time);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static void main(String[] args) {
//        DateTime dateTime = new DateTime("25.06.23 01:00", 90);
//        System.out.println(dateTime.start + " " + dateTime.end);
//        System.out.println(dateTime.getOutputStart() + " " + dateTime.getOutputEnd());
//        System.out.println(formatToConvenient(dateTime.getOutputStart()) + " " +
//                formatToConvenient(dateTime.getOutputEnd()));
    }
}

