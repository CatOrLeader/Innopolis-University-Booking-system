package Database.Services;

import Models.UserDataModel;
import dialog.data.BotState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;

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
     * @param userData model of user's data
     */
    public void addUserData(UserDataModel userData) {
        String query = "INSERT INTO \"TgChat\" (\"Id\", \"UserEmail\", \"IsAuthorized\", \"BotState\", \"Language\") VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userData.userId);
            statement.setString(2, userData.email);
            statement.setBoolean(3, userData.isAuthorized);
            statement.setObject(4, userData.dialogState, Types.OTHER);
            statement.setObject(5, userData.language, Types.OTHER);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates data about telegram bot user
     *
     * @param userData model of user's data
     */
    public void updateUserData(UserDataModel userData) {
        String query = "UPDATE \"TgChat\" SET \"UserEmail\" = ?, \"IsAuthorized\" = ?, " +
                "\"BotState\" = ?, \"Language\" = ? WHERE \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userData.email);
            statement.setBoolean(2, userData.isAuthorized);
            statement.setObject(3, userData.dialogState, Types.OTHER);
            statement.setObject(4, userData.language, Types.OTHER);
            statement.setLong(5, userData.userId);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates user's data
     *
     * @param tgChatId user's chat id
     * @return model of the user
     */
    public UserDataModel getUserData(long tgChatId) {
        String query = "SELECT \"UserEmail\", \"IsAuthorized\", \"BotState\", \"Language\" " +
                "FROM \"TgChat\" WHERE \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, tgChatId);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String email = resultSet.getString("UserEmail");
                boolean isAuthorized = resultSet.getBoolean("IsAuthorized");
                String botStateName = resultSet.getString("BotState");
                BotState botState = BotState.valueOf(botStateName);
                String language = resultSet.getString("Language");

                return new UserDataModel(tgChatId, email, isAuthorized, botState, language);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
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
