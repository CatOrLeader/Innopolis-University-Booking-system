package Database.Services;

import Models.BookingModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;

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
     * @param bookingModel model of the booking
     */
    public void addNewBooking(BookingModel bookingModel) {
        String query = "INSERT INTO \"Booking\" (\"Id\", \"TgChatId\", \"Title\", \"RoomId\", \"Start\", \"End\", \"isConfirmed\") VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, bookingModel.id);
            statement.setLong(2, bookingModel.tgChatId);
            statement.setString(3, bookingModel.title);
            statement.setString(4, bookingModel.roomId);
            statement.setTimestamp(5, bookingModel.start);
            statement.setTimestamp(6, bookingModel.end);
            statement.setBoolean(7, bookingModel.isConfirmed);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates particular booking data
     *
     * @param bookingModel model of the booking
     */
    public void updateBooking(BookingModel bookingModel) {
        String query = "UPDATE \"Booking\" SET \"TgChatId\" = ?, \"Title\" = ?, " +
                "\"RoomId\" = ?, \"Start\" = ?, \"End\" = ?, \"isConfirmed\" = ? " +
                " WHERE \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, bookingModel.tgChatId);
            statement.setString(2, bookingModel.title);
            statement.setString(3, bookingModel.roomId);
            statement.setTimestamp(4, bookingModel.start);
            statement.setTimestamp(5, bookingModel.end);
            statement.setBoolean(6, bookingModel.isConfirmed);
            statement.setString(7, bookingModel.id);
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
    public BookingModel getBookingDataById(String bookingId) {
        String query = "SELECT \"TgChatId\", \"Title\", \"RoomId\", \"Start\", \"End\", \"isConfirmed\" " +
                "FROM \"Booking\" WHERE \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, bookingId);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                long tgChatId = resultSet.getLong("TgChatId");
                String title = resultSet.getString("Title");
                String roomId = resultSet.getString("RoomId");
                Timestamp start = resultSet.getTimestamp("Start");
                Timestamp end = resultSet.getTimestamp("End");
                boolean isConfirmed = resultSet.getBoolean("isConfirmed");

                return new BookingModel(bookingId, tgChatId, title, roomId, start, end, isConfirmed);
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
     * @param tgChatId ID of the user chat
     * @return list of user's bookings
     */
    public ArrayList<BookingModel> getBookingsByUserChatId(long tgChatId) {
        ArrayList<BookingModel> bookings = new ArrayList<>();
        String query = "SELECT \"Id\", \"Title\", \"RoomId\", \"Start\", \"End\", \"isConfirmed\" " +
                "FROM \"Booking\" WHERE \"TgChatId\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, tgChatId);
            var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("Id");
                String title = resultSet.getString("Title");
                String roomId = resultSet.getString("RoomId");
                Timestamp start = resultSet.getTimestamp("Start");
                Timestamp end = resultSet.getTimestamp("End");
                boolean isConfirmed = resultSet.getBoolean("isConfirmed");

                BookingModel booking = new BookingModel(id, tgChatId, title, roomId, start, end, isConfirmed);
                bookings.add(booking);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return bookings;
    }

    /**
     * Filter for bookings by time
     *
     * @param start start date time (nullable)
     * @param end   end date time (nullable)
     * @return list of bookings
     */
    public ArrayList<BookingModel> getBookingsByTimePeriod(Timestamp start, Timestamp end) {
        ArrayList<BookingModel> bookings = new ArrayList<>();
        String query = "SELECT \"Id\", \"TgChatId\", \"Title\", \"RoomId\", \"Start\", \"End\", \"isConfirmed\" " +
                "FROM \"Booking\" WHERE 1=1";

        if (start != null) {
            query += " AND \"Start\" >= ?";
        }
        if (end != null) {
            query += " AND \"End\" <= ?";
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
                long tgChatId = resultSet.getLong("TgChatId");
                String title = resultSet.getString("Title");
                String roomId = resultSet.getString("RoomId");
                Timestamp bookingStart = resultSet.getTimestamp("Start");
                Timestamp bookingEnd = resultSet.getTimestamp("End");
                boolean isConfirmed = resultSet.getBoolean("isConfirmed");

                BookingModel booking = new BookingModel(id, tgChatId, title, roomId, bookingStart, bookingEnd, isConfirmed);
                bookings.add(booking);
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
