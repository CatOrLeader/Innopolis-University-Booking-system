package Models;

import APIWrapper.json.Room;

/**
 * Room representation in the database
 */
public class RoomModel {
    public final String id;
    public String name;
    public int capacity;
    public RoomType roomType;

    public RoomModel(String id, String name, int capacity, RoomType roomType) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.roomType = roomType;
    }

    public Room toRoom() {
        return new Room(name, id, toJsonType(roomType), capacity);
    }

    // TODO: get rid of strange code duplication
    private APIWrapper.json.RoomType toJsonType(RoomType type) {
        return switch (type) {
            case MEETING_ROOM -> APIWrapper.json.RoomType.MEETING_ROOM;
            case AUDITORIUM -> APIWrapper.json.RoomType.AUDITORIUM;
        };
    }
}
