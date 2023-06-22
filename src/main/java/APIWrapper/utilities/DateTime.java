package APIWrapper.utilities;

import APIWrapper.json.Booking;
import APIWrapper.json.Room;
import APIWrapper.json.RoomType;
import com.google.gson.GsonBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTime {
    // Initial values
    private LocalDateTime start;
    private LocalDateTime end;
    private Duration duration;

    // Additional values
    private static final String inputFormat = "dd.MM.yy HH:mm";
    // TODO: Check the format conditions according to the customers' request
    private static final String outputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public DateTime(String start, String end) {
        this.start = LocalDateTime.parse(start, DateTimeFormatter.ofPattern(inputFormat));
        this.end = LocalDateTime.parse(end, DateTimeFormatter.ofPattern(inputFormat));
        this.duration = Duration.between(this.start, this.end);
    }

    public DateTime(String start, int duration) {
        this.start = LocalDateTime.parse(start, DateTimeFormatter.ofPattern(inputFormat));
        this.duration = Duration.of(duration, ChronoUnit.MINUTES);
        this.end = this.start.plusMinutes(this.duration.toMinutes());
    }

    // Setters
    public void setEnd(String end) {
        this.end = LocalDateTime.parse(end, DateTimeFormatter.ofPattern(inputFormat));
        duration = Duration.between(this.start, this.end);
    }

    public void setDuration(int duration) {
        this.duration = Duration.of(duration, ChronoUnit.MINUTES);
        this.end = this.start.plusMinutes(this.duration.toMinutes());
    }

    // Inner methods of the date and time formatter
    public String getOutputStart() {
        return start.format(DateTimeFormatter.ofPattern(outputFormat));
    }

    public String getOutputEnd() {
        return end.format(DateTimeFormatter.ofPattern(outputFormat));
    }

    // Outer methods for parsing incoming string time to user-friendly view
    public static String formatToConvenient(String time) {
        return
                LocalDateTime
                        .parse(time, DateTimeFormatter.ofPattern(outputFormat))
                        .format(DateTimeFormatter.ofPattern(inputFormat));

    }

    public static void main(String[] args) {
        Booking booking = new Booking(
                "Matvey privet!",
                "Zatesti menya pzh",
                "22.06.23 05:00",
                90,
                new Room(
                        "hui",
                        "23112",
                        RoomType.MEETING_ROOM,
                        20
                ),
                "sda"
        );

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(booking));


        System.out.println(DateTime.formatToConvenient("2023-06-22T00:28:22.135Z"));
    }
}

// Exceptions

