package Database.Controllers;

import Database.DbConnector;
import Database.Services.RoomService;
import Models.RoomModel;

/**
 * Controller for the rooms
 */
public class RoomController {
    // database connection
    private final DbConnector connection = new DbConnector();
    private final RoomService roomService;

    public RoomController() {
        roomService = new RoomService(connection.getConnection());
    }

    /**
     * Adds a new room to the database or updates the existing one
     *
     * @param roomModel model of the room
     */
    public void addOrUpdateRoom(RoomModel roomModel) {
        if (roomService.roomExists(roomModel.id)) {
            roomService.updateRoomData(roomModel);
        } else {
            roomService.addNewRoom(roomModel);
        }
    }

    /**
     * Returns data about the room
     *
     * @param roomId room's id
     * @return model of the room
     */
    public RoomModel getRoomData(String roomId) {
        return roomService.getRoomData(roomId);
    }

    /**
     * Checks if the room exists
     *
     * @param roomId id of the room
     * @return true if room is already exists in the database, false otherwise
     */
    public boolean roomExists(String roomId) {
        return roomService.roomExists(roomId);
    }
}
