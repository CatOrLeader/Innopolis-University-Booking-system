package Database.Services;

import Models.RoomModel;
import Models.RoomType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;

/**
 * Class for the room data access in database
 */
public class RoomService {
    // db connection
    private final Connection connection;

    /**
     * Constructor with connection to the database
     *
     * @param connection connection to the database
     */
    public RoomService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Adds a new room to the database
     *
     * @param roomModel model of the room
     */
    public void addNewRoom(RoomModel roomModel) {
        String query = "INSERT INTO \"Room\" (\"Id\", \"Name\", \"Capacity\", \"RoomType\") VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, roomModel.id);
            statement.setString(2, roomModel.name);
            statement.setInt(3, roomModel.capacity);
            statement.setObject(4, roomModel.roomType, Types.OTHER);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates particular room data
     *
     * @param roomModel model of the room
     */
    public void updateRoomData(RoomModel roomModel) {
        String query = "UPDATE \"Room\" SET \"Name\" = ?, \"Capacity\" = ?, " +
                "\"RoomType\" = ? WHERE \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, roomModel.name);
            statement.setInt(2, roomModel.capacity);
            statement.setObject(3, roomModel.roomType, Types.OTHER);
            statement.setString(4, roomModel.id);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns data about the room
     *
     * @param roomId room's id
     * @return model of the room
     */
    public RoomModel getRoomData(String roomId) {
        String query = "SELECT \"Name\", \"Capacity\", \"RoomType\" " +
                "FROM \"Room\" WHERE \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, roomId);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("Name");
                int capacity = resultSet.getInt("Capacity");
                RoomType roomType = RoomType.valueOf(resultSet.getString("RoomType"));

                return new RoomModel(roomId, name, capacity, roomType);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Checks if the room exists
     *
     * @param roomId id of the room
     * @return true if room is already exists in the database, false otherwise
     */
    public boolean roomExists(String roomId) {
        String query = "SELECT COUNT(*) FROM \"Room\" WHERE \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, roomId);
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
}
