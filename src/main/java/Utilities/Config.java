package Utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Class to store external configuration constants.
 */
public class Config {
    /**
     * Properties initialization.
     */
    private static final Properties properties;
    static {
        var propsPath = Path.of("properties.cfg");
        try {
            var stream = Files.newBufferedReader(propsPath);
            properties = new Properties();
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Integer> bookingDurations;
    private static Integer bookingSafePeriod;
    private static Integer bookingReminderPeriod;

    /**
     * Get list of available bookings durations in minutes
     * @return list of bookings durations
     */
    public static List<Integer> bookingDurations() {
        if (bookingDurations != null) {
            return bookingDurations;
        }
        var durationsString = (String) properties.get("booking_durations");
        bookingDurations =  Arrays.stream(
                durationsString.strip().split(",")
        ).map(duration -> Integer.parseInt(duration.strip())).toList();
        return bookingDurations;
    }

    /**
     * Get booking safe period
     * @return booking safe period
     */
    public static Integer bookingSafePeriod() {
        if (bookingSafePeriod != null) {
            return bookingSafePeriod;
        }
        bookingSafePeriod = Integer.parseInt((String) properties.get("booking_safe_period"));
        return bookingSafePeriod;
    }

    /**
     * Get booking reminder period
     * @return booking reminder period
     */
    public static Integer bookingReminderPeriod() {
        if (bookingReminderPeriod != null) {
            return bookingReminderPeriod;
        }
        bookingReminderPeriod = Integer.parseInt((String) properties.get("booking_reminder_period"));
        return bookingReminderPeriod;
    }
}
