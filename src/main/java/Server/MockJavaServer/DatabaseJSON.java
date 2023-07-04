package Server.MockJavaServer;

import APIWrapper.Json.Booking;
import APIWrapper.Json.Room;

import java.util.ArrayList;

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
