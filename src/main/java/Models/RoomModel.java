package Models;

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
}
