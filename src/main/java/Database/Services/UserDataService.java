package Database.Services;

import Bot.Dialog.Config.EnglishText;
import Bot.Dialog.Config.RussianText;
import Bot.Dialog.Data.BotState;
import Bot.Dialog.Data.UserData;

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
    public void addUserData(UserData userData) {
        String query = "INSERT INTO \"TgChat\" (\"Id\", \"UserEmail\", \"IsAuthorized\", \"BotState\", \"Language\") " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userData.getUserId());
            statement.setString(2, userData.getEmail());
            statement.setBoolean(3, userData.isAuthorized());
            statement.setObject(4, userData.getDialogState(), Types.OTHER);
            statement.setObject(5, userData.getLang().abbreviation(), Types.OTHER);
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
    public void updateUserData(UserData userData) {
        String query = "UPDATE \"TgChat\" SET \"UserEmail\" = ?, \"IsAuthorized\" = ?, " +
                "\"BotState\" = ?, \"Language\" = ? WHERE \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userData.getEmail());
            statement.setBoolean(2, userData.isAuthorized());
            statement.setObject(3, userData.getDialogState(), Types.OTHER);
            statement.setObject(4, userData.getLang().abbreviation(), Types.OTHER);
            statement.setLong(5, userData.getUserId());
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates user's data
     *
     * @param userId user's chat id
     * @return model of the user
     */
    public UserData getUserData(long userId) {
        String query = "SELECT \"UserEmail\", \"IsAuthorized\", \"BotState\", \"Language\" " +
                "FROM \"TgChat\" WHERE \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String email = resultSet.getString("UserEmail");
                boolean isAuthorized = resultSet.getBoolean("IsAuthorized");
                String botStateName = resultSet.getString("BotState");
                BotState botState = BotState.valueOf(botStateName);
                String language = resultSet.getString("Language");

                return new UserData(userId, email, isAuthorized, botState,
                        language.equals("RUS") ? new RussianText() : new EnglishText());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Checks if user exists in database
     *
     * @param userId user's chat id
     * @return true if user exists, false otherwise
     */
    public boolean userExists(long userId) {
        String query = "SELECT COUNT(*) FROM \"TgChat\" WHERE \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
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
     * @param userId user's chat id
     * @return true if user exists, false otherwise
     */
    public boolean isAuthorized(long userId) {
        String query = "SELECT \"IsAuthorized\" FROM \"TgChat\" WHERE \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
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
