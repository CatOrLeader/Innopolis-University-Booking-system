package Utilities;

import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;

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

    @Nullable
    public static Boolean isValid(String time) {
        Boolean isIdentified = identifyAndValidate(time);

        if (isIdentified == null) {
            System.out.println("DateTime can't be recognized by the bot! Check the format properly");
            System.out.println("Given: " + time);
        }

        return isIdentified;
    }

    private static Boolean identifyAndValidate(String time) {
        if (isCustomFormat(time)) {
            return isValidCustomFormat(time);
        }

        if (isIsoInstant(time)) {
            return isValidIsoInstant(time);
        }

        return null;
    }

    private static boolean isCustomFormat(String time) {
        try {
            LocalDateTime suspect = LocalDateTime.parse(
                    time,
                    INPUT_FORMATTER.withResolverStyle(ResolverStyle.STRICT)
            );

            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static boolean isValidCustomFormat(String time) {
        LocalDateTime suspect;

        try {
            suspect = LocalDateTime.parse(
                    time,
                    INPUT_FORMATTER.withResolverStyle(ResolverStyle.STRICT)
            );
        } catch (DateTimeParseException e) {
            System.out.println("DateTime log\n--> incorrect format of the string: " + time);
            return false;
        }

        if (LocalDateTime.now().isAfter(suspect)) {
            System.out.println("DateTime Log\n" +
                    "--> Booking in the past is prohibited (*_*) ----\n" +
                    "Now: " + LocalDateTime.now().format(INPUT_FORMATTER) + "\n__vs.__\nGiven: " + time);
            return false;
        }

        if (suspect.getMinute() % 15 != 0) {
            System.out.println("DateTime Log\n" +
                    "--> Booking time is not satisfy the criterion (division by 15 w\\out reminder)     ----\n" +
                    "Given: " + time);
            return false;
        }

        return true;
    }

    private static boolean isIsoInstant(String time) {
        try {
            LocalDateTime local = LocalDateTime.ofInstant(
                    Instant.from(OUTPUT_FORMATTER.parse(time)), ZoneOffset.UTC
            );

            ZonedDateTime suspect = ZonedDateTime.of(
                    local,
                    ZoneOffset.UTC
            );

            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static boolean isValidIsoInstant(String time) {
        ZonedDateTime suspect;

        try {
            LocalDateTime local = LocalDateTime.ofInstant(
                    Instant.from(OUTPUT_FORMATTER.parse(time)), ZoneOffset.UTC
            );

            suspect = ZonedDateTime.of(
                    local,
                    ZoneOffset.UTC
            );
        } catch (DateTimeParseException e) {
            System.out.println("DateTime log\n--> incorrect format of the string: " + e.getParsedString());
            System.out.println(e.getCause().getMessage());
            return false;
        }

        if (ZonedDateTime.now().isAfter(suspect)) {
            System.out.println("DateTime Log\n" +
                    "--> Booking in the past is prohibited (*_*) ----\n" +
                    "Now: " + ZonedDateTime.now().format(OUTPUT_FORMATTER) + "\n__vs.__\nGiven: " + time);
            return false;
        }

        if (suspect.getMinute() % 15 != 0) {
            System.out.println("DateTime Log\n" +
                    "--> Booking time is not satisfy the criterion (division by 15 w\\out reminder) ----\n" +
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

    public static Timestamp parseISOToTimestamp(String time) {
        return Timestamp.valueOf(ZonedDateTime.of(
                LocalDateTime.ofInstant(
                        Instant.from(DateTimeFormatter.ISO_INSTANT.parse(time)),
                        ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES),
                ZoneOffset.UTC
        ).toLocalDateTime());
    }

    public static String formatTimestampToISO(Timestamp timestamp) {
        return timestamp.
                toLocalDateTime().
                atZone(ZoneId.systemDefault()).
                plusHours(3).
                format(DateTimeFormatter.ISO_INSTANT);
    }
}

