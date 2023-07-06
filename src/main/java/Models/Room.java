package Models;

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
}
