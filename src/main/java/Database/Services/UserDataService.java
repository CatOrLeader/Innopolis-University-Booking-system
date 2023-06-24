package Database.Services;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Class for implementation user data access in database
 */
public class UserDataService {
    // db connection
    private final Connection connection;

    /**
     * Constructor with connection to the database
     *
     * @param connection connection to the database
     */
    public UserDataService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Adds a new bot user to the database
     *
     * @param tgChatId     user's chat id
     * @param email        user's email
     * @param isAuthorized email confirmation
     */
    public void addUserData(long tgChatId, String email, boolean isAuthorized) {
        String query = "INSERT INTO \"TgChat\" (\"Id\", \"UserEmail\", \"IsAuthorized\") VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, tgChatId);
            statement.setString(2, email);
            statement.setBoolean(3, isAuthorized);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates data about telegram bot user
     *
     * @param tgChatId     user's chat id
     * @param email        user's email
     * @param isAuthorized email confirmation
     */
    public void updateUserData(long tgChatId, String email, boolean isAuthorized) {
        String query = "UPDATE \"TgChat\" SET \"UserEmail\" = ?, \"IsAuthorized\" = ? " +
                "WHERE \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setBoolean(2, isAuthorized);
            statement.setLong(3, tgChatId);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Checks if user exists in database
     *
     * @param tgChatId user's chat id
     * @return true if user exists, false otherwise
     */
    public boolean userExists(long tgChatId) {
        String query = "SELECT COUNT(*) FROM \"TgChat\" WHERE \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, tgChatId);
            var result = statement.executeQuery();

            if (result.next()) {
                int count = result.getInt(1);
                return count > 0;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    /**
     * Checks if user authorized
     *
     * @param tgChatId user's chat id
     * @return true if user exists, false otherwise
     */
    public boolean isAuthorized(long tgChatId) {
        String query = "SELECT \"IsAuthorized\" FROM \"TgChat\" WHERE \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, tgChatId);
            var result = statement.executeQuery();

            if (result.next()) {
                return result.getBoolean(1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }
}
