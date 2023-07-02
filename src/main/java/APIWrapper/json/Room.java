package APIWrapper.json;

import Models.RoomModel;

public class Room {
    public String name;
    public String id;
    public RoomType type;
    public int capacity;

    public Room(String name, String id, RoomType type, int capacity) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.capacity = capacity;
    }

    // TODO: get rid of strange code duplication
    public RoomModel toRoomModel() {
        return new RoomModel(id, name, capacity, toDatabaseType(type));
    }

    private Models.RoomType toDatabaseType(RoomType type) {
        return switch (type) {
            case MEETING_ROOM -> Models.RoomType.MEETING_ROOM;
            case AUDITORIUM -> Models.RoomType.AUDITORIUM;
        };
    }
}
