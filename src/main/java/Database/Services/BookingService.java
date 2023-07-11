package Database.Services;

import Models.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;

import static Utilities.DateTime.formatTimestampToISO;
import static Utilities.DateTime.parseISOToTimestamp;

/**
 * Class for the bookings data access in database
 */
public class BookingService {
    // db connection
    private final Connection connection;

    /**
     * Constructor with connection to the database
     *
     * @param connection connection to the database
     */
    public BookingService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Adds a new booking to the database
     *
     * @param booking model of the booking
     */
    public void addNewBooking(Booking booking) {
        String query = "INSERT INTO \"Booking\" (\"Id\", \"TgChatId\", \"Title\", \"RoomId\", \"Start\", \"End\", \"isConfirmed\") VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, booking.id);
            statement.setLong(2, booking.userId);
            statement.setString(3, booking.title);
            statement.setString(4, booking.room.id);
            statement.setTimestamp(5, parseISOToTimestamp(booking.start));
            statement.setTimestamp(6, parseISOToTimestamp(booking.end));
            statement.setBoolean(7, booking.isConfirmed);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates particular booking data
     *
     * @param booking model of the booking
     */
    public void updateBooking(Booking booking) {
        String query = "UPDATE \"Booking\" SET \"TgChatId\" = ?, \"Title\" = ?, " +
                "\"RoomId\" = ?, \"Start\" = ?, \"End\" = ?, \"isConfirmed\" = ? " +
                " WHERE \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, booking.userId);
            statement.setString(2, booking.title);
            statement.setString(3, booking.room.id);
            statement.setTimestamp(4, parseISOToTimestamp(booking.start));
            statement.setTimestamp(5, parseISOToTimestamp(booking.end));
            statement.setBoolean(6, booking.isConfirmed);
            statement.setString(7, booking.id);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns data about the booking
     * by booking id
     *
     * @param bookingId id of the booking
     * @return model of the booking
     */
    public Booking getBookingDataById(String bookingId) {
        String query = "SELECT b.\"TgChatId\", b.\"Title\", b.\"RoomId\", b.\"Start\", b.\"End\", b.\"isConfirmed\", tg.\"UserEmail\" " +
                "FROM \"Booking\" b " +
                "JOIN \"TgChat\" tg ON b.\"TgChatId\" = tg.\"Id\" " +
                "WHERE b.\"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, bookingId);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                long userId = resultSet.getLong("TgChatId");
                String userEmail = resultSet.getString("UserEmail");
                String title = resultSet.getString("Title");
                String roomId = resultSet.getString("RoomId");
                Timestamp start = resultSet.getTimestamp("Start");
                Timestamp end = resultSet.getTimestamp("End");
                boolean isConfirmed = resultSet.getBoolean("isConfirmed");

                return new Booking(bookingId,
                        userId,
                        title,
                        formatTimestampToISO(start),
                        formatTimestampToISO(end),
                        roomId,
                        userEmail,
                        isConfirmed);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Returns data about the bookings
     * by user's chat ID
     *
     * @param UserId ID of the user chat
     * @return list of user's bookings
     */
    public ArrayList<Booking> getBookingsByUserChatId(long UserId) {
        ArrayList<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.\"Id\", b.\"Title\", b.\"RoomId\", b.\"Start\", b.\"End\", b.\"isConfirmed\", tg.\"UserEmail\" " +
                "FROM \"Booking\" b " +
                "JOIN \"TgChat\" tg ON b.\"TgChatId\" = tg.\"Id\" " +
                "WHERE b.\"TgChatId\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, UserId);
            var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("Id");
                String userEmail = resultSet.getString("UserEmail");
                String title = resultSet.getString("Title");
                String roomId = resultSet.getString("RoomId");
                Timestamp start = resultSet.getTimestamp("Start");
                Timestamp end = resultSet.getTimestamp("End");
                boolean isConfirmed = resultSet.getBoolean("isConfirmed");

                bookings.add(new Booking(id,
                        UserId,
                        title,
                        formatTimestampToISO(start),
                        formatTimestampToISO(end),
                        roomId,
                        userEmail,
                        isConfirmed));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return bookings;
    }

    /**
     * Filter for bookings by time period
     *
     * @param start start date time (nullable)
     * @param end   end date time (nullable)
     * @return list of bookings
     */
    public ArrayList<Booking> getBookingsByTimePeriod(Timestamp start, Timestamp end) {
        ArrayList<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.\"Id\", b.\"TgChatId\", b.\"Title\", b.\"RoomId\", b.\"Start\", b.\"End\", b.\"isConfirmed\", tg.\"UserEmail\" " +
                "FROM \"Booking\" b " +
                "JOIN \"TgChat\" tg ON b.\"TgChatId\" = tg.\"Id\" " +
                "WHERE 1=1";

        if (start != null) {
            query += " AND b.\"Start\" >= ?";
        }
        if (end != null) {
            query += " AND b.\"End\" <= ?";
        }

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int parameterIndex = 1;

            if (start != null) {
                statement.setTimestamp(parameterIndex++, start);
            }
            if (end != null) {
                statement.setTimestamp(parameterIndex++, end);
            }

            var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("Id");
                long userId = resultSet.getLong("TgChatId");
                String userEmail = resultSet.getString("UserEmail");
                String title = resultSet.getString("Title");
                String roomId = resultSet.getString("RoomId");
                Timestamp bookingStart = resultSet.getTimestamp("Start");
                Timestamp bookingEnd = resultSet.getTimestamp("End");
                boolean isConfirmed = resultSet.getBoolean("isConfirmed");

                bookings.add(new Booking(id,
                        userId,
                        title,
                        formatTimestampToISO(bookingStart),
                        formatTimestampToISO(bookingEnd),
                        roomId,
                        userEmail,
                        isConfirmed));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return bookings;
    }

    /**
     * Filter for bookings by time start
     *
     * @param start start date time
     * @return list of bookings
     */
    public ArrayList<Booking> getBookingsByTimeStart(Timestamp start) {
        ArrayList<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.\"Id\", b.\"TgChatId\", b.\"Title\", b.\"RoomId\", b.\"Start\", b.\"End\", b.\"isConfirmed\", tg.\"UserEmail\" " +
                "FROM \"Booking\" b " +
                "JOIN \"TgChat\" tg ON b.\"TgChatId\" = tg.\"Id\" " +
                "WHERE b.\"Start\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, start);

            var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("Id");
                long userId = resultSet.getLong("TgChatId");
                String userEmail = resultSet.getString("UserEmail");
                String title = resultSet.getString("Title");
                String roomId = resultSet.getString("RoomId");
                Timestamp bookingStart = resultSet.getTimestamp("Start");
                Timestamp bookingEnd = resultSet.getTimestamp("End");
                boolean isConfirmed = resultSet.getBoolean("isConfirmed");

                bookings.add(new Booking(id,
                        userId,
                        title,
                        formatTimestampToISO(bookingStart),
                        formatTimestampToISO(bookingEnd),
                        roomId,
                        userEmail,
                        isConfirmed));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return bookings;
    }

    /**
     * Confirms booking
     *
     * @param bookingId id of the booking
     */
    public void confirmBooking(String bookingId) {
        String query = "UPDATE \"Booking\" SET \"isConfirmed\" = true WHERE \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, bookingId);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes booking
     *
     * @param bookingId id of the booking
     */
    public void deleteBooking(String bookingId) {
        String query = "DELETE from \"Booking\"  WHERE \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, bookingId);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
