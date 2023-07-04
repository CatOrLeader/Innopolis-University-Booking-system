package mockJavaServer;

import APIWrapper.json.Booking;
import APIWrapper.json.Room;
import APIWrapper.json.RoomType;
import APIWrapper.utilities.DateTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

class Database {
    public final ArrayList<Room> rooms;
    public final ArrayList<Room> freeRooms;
    public final ArrayList<Booking> bookings;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Database() {
        rooms = createRooms();
        freeRooms = createFreeRooms();
        bookings = createBookings();
    }

    // JSON Parsing
    private String parseToGsonRooms(ArrayList<Room> rooms) {
        Type type = new TypeToken<ArrayList<Room>>() {
        }.getType();
        return gson.toJson(rooms, type);
    }

    private String parseToGsonBookings(ArrayList<Booking> bookings) {
        Type type = new TypeToken<ArrayList<Booking>>() {
        }.getType();
        return gson.toJson(bookings, type);
    }

    // Rooms creating
    private ArrayList<Room> createRooms() {
        ArrayList<Room> rooms = new ArrayList<>();

        rooms.add(new Room(
                "Meeting Room #3.2",
                "3.2",
                RoomType.MEETING_ROOM,
                6)
        );

        rooms.add(new Room(
                "Meeting Room #3.3",
                "3.3",
                RoomType.MEETING_ROOM,
                6)
        );

        rooms.add(new Room(
                "Meeting Room #3.4",
                "3.4",
                RoomType.MEETING_ROOM,
                6)
        );

        rooms.add(new Room(
                "Lecture Room 108",
                "108",
                RoomType.AUDITORIUM,
                250)
        );

        rooms.add(new Room(
                "Lecture Room 107",
                "107",
                RoomType.AUDITORIUM,
                180)
        );

        rooms.add(new Room(
                "Meeting Room #3.1",
                "3.1",
                RoomType.MEETING_ROOM,
                5)
        );

        rooms.add(new Room(
                "Meeting Room #3.5",
                "3.5",
                RoomType.MEETING_ROOM,
                7)
        );


        rooms.add(new Room(
                "Lecture Room 100",
                "100",
                RoomType.AUDITORIUM,
                90)
        );

        return rooms;
    }

    // Free rooms creating
    private ArrayList<Room> createFreeRooms() {
        ArrayList<Room> rooms = new ArrayList<>();

        rooms.add(new Room(
                "Meeting Room #3.1",
                "3.1",
                RoomType.MEETING_ROOM,
                5)
        );

        rooms.add(new Room(
                "Meeting Room #3.5",
                "3.5",
                RoomType.MEETING_ROOM,
                7)
        );


        rooms.add(new Room(
                "Lecture Room 100",
                "100",
                RoomType.AUDITORIUM,
                90)
        );

        return rooms;
    }

    // Bookings creating
    private ArrayList<Booking> createBookings() {
        return new ArrayList<>();
    }

    // Rooms processes
    public void makeBusy(String roomId) {
        boolean flag = freeRooms.removeIf(room -> room.id.equals(roomId));

        if (!flag) {
            System.out.println("Incorrect room id");
        }
    }

    public void makeFree(String roomId) {
        for (Room room : rooms) {
            if (room.id.equals(roomId)) {
                freeRooms.add(room);
            }
        }
    }

    public Room getRoom(String roomId) {
        for (Room room : freeRooms) {
            if (room.id.equals(roomId)) {
                return room;
            }
        }

        return null;
    }

    // Bookings processes
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public boolean deleteBooking(String bookingId) {

        return bookings.removeIf(booking -> booking.id.equals(bookingId));
    }

    public Booking getBookings(String bookingId) {
        for (Booking booking : bookings) {
            if (booking.id.equals(bookingId)) {
                return booking;
            }
        }

        return null;
    }

    public ArrayList<Booking> takeNeededBookings(DateTime dateTime, ArrayList<String> roomIds,
                                                 ArrayList<String> emails) {
        ArrayList<Booking> output = new ArrayList<>();

        for (Booking booking : bookings) {
            if (emails.contains(booking.owner_email)) {
                output.add(booking);
            }
        }

        return output;
    }
}
