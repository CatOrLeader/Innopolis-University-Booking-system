package mockJavaServer;

import java.util.ArrayList;
import APIWrapper.json.Booking;
import APIWrapper.json.Room;

class DatabaseJSON {
    public final ArrayList<Room> rooms;
    public final ArrayList<Room> free;
    public final ArrayList<Booking> bookings;

    public DatabaseJSON(ArrayList<Room> rooms, ArrayList<Room> free, ArrayList<Booking> bookings) {
        this.rooms = rooms;
        this.free = free;
        this.bookings = bookings;
    }
}
