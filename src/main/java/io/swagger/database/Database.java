package io.swagger.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.model.*;
import org.threeten.bp.OffsetDateTime;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Database {
    public final ArrayList<Room> rooms;
    public final ArrayList<Room> freeRooms;
    public final ArrayList<Booking> bookings;
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    // Singleton creation
    private Database() {
        rooms = createRooms();
        freeRooms = createFreeRooms();
        bookings = createBookings();
    }

    private static class Holder {
        private static final Database INSTANCE = new Database();
    }

    public static Database getInstance() {
        return Holder.INSTANCE;
    }

    // JSON Parsing
    private String parseToGsonRooms(ArrayList<Room> rooms) {
        try {
            return mapper.writeValueAsString(rooms);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private String parseToGsonBookings(ArrayList<Booking> bookings) {
        try {
            return mapper.writeValueAsString(bookings);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // Rooms creating
    private ArrayList<Room> createRooms() {
        ArrayList<Room> rooms = new ArrayList<>();

        rooms.add(new Room()
                .name("Meeting Room #3.2")
                .id("3.2")
                .type(RoomType.MEETING_ROOM)
                .capacity(6));

        rooms.add(new Room()
                .name("Meeting Room #3.3")
                .id("3.3")
                .type(RoomType.MEETING_ROOM)
                .capacity(6));

        rooms.add(new Room()
                .name("Meeting Room #3.4")
                .id("3.4")
                .type(RoomType.MEETING_ROOM)
                .capacity(6));

        rooms.add(new Room()
                .name("Lecture Room 108")
                .id("108")
                .type(RoomType.AUDITORIUM)
                .capacity(250));

        rooms.add(new Room()
                .name("Lecture Room 107")
                .id("107")
                .type(RoomType.AUDITORIUM)
                .capacity(180));

        rooms.add(new Room()
                .name("Meeting Room #3.1")
                .id("3.1")
                .type(RoomType.MEETING_ROOM)
                .capacity(5));

        rooms.add(new Room()
                .name("Meeting Room #3.5")
                .id("3.5")
                .type(RoomType.MEETING_ROOM)
                .capacity(6));


        rooms.add(new Room()
                .name("Lecture Room 100")
                .id("100")
                .type(RoomType.AUDITORIUM)
                .capacity(90));

        return rooms;
    }

    // Free rooms creating
    private ArrayList<Room> createFreeRooms() {
        return createRooms();
    }

    public ArrayList<Room> getFreeRoomsInTime(GetFreeRoomsRequest request) {
        OffsetDateTime start = request.getStart();
        OffsetDateTime end = request.getEnd();

        ArrayList<Room> possibleFreeRooms = new ArrayList<>(rooms);
        for (Booking booking : bookings) {
            if (
                    (start.isBefore(booking.getStart()) && end.isAfter(booking.getStart()))
                    || (start.isAfter(booking.getStart()) && end.isBefore(booking.getEnd()))
                    || (start.isBefore(booking.getEnd()) && end.isAfter(booking.getEnd())
                    || start.isEqual(booking.getStart()) || end.isEqual(booking.getEnd()))
            ) {
                possibleFreeRooms.remove(booking.getRoom());
            }
        }

        return possibleFreeRooms;
    }

    // Bookings creating
    private ArrayList<Booking> createBookings() {
        return new ArrayList<>();
    }

    public Booking createBooking(String roomId, BookRoomRequest request) throws BookRoomException {
        Room room = getRoom(roomId);
        if (!isFree(room, request)) throw new BookRoomException();

        Booking booking = new Booking()
                .id(String.valueOf(bookings.size() + 1))
                .title(request.getTitle())
                .start(request.getStart())
                .end(request.getEnd())
                .room(room)
                .ownerEmail(request.getOwnerEmail());

        bookings.add(booking);
        makeBusy(roomId);

        return booking;
    }

    // Rooms processes
    private boolean isFree(Room room, BookRoomRequest request) {
        OffsetDateTime start = request.getStart();
        OffsetDateTime end = request.getEnd();

        for (Booking booking : bookings) {
            if (booking.getRoom().getId().equals(room.getId())) {
                if (
                        (start.isBefore(booking.getStart()) && end.isAfter(booking.getStart()))
                        || (start.isAfter(booking.getStart()) && end.isBefore(booking.getEnd()))
                        || (start.isBefore(booking.getEnd()) && end.isAfter(booking.getEnd()))
                        || start.isEqual(booking.getStart()) || end.isEqual(booking.getEnd())
                ) {
                    return false;
                }
            }
        }

        return true;
    }

    public void makeBusy(String roomId) {
        boolean flag = freeRooms.removeIf(room -> room.getId().equals(roomId));

        if (!flag) {
            System.out.println("Incorrect room id");
        }
    }

    public void makeFree(String roomId) {
        for (Room room : rooms) {
            if (room.getId().equals(roomId)) {
                freeRooms.add(room);
            }
        }
    }

    public Room getRoom(String roomId) {
        for (Room room : rooms) {
            if (room.getId().equals(roomId)) {
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

        return bookings.removeIf(booking -> booking.getId().equals(bookingId));
    }

    private Booking getBooking(String bookingId) {
        for (Booking booking : bookings) {
            if (booking.getId().equals(bookingId)) {
                return booking;
            }
        }

        return null;
    }

    public ArrayList<Booking> takeNeededBookings(QueryBookingsRequest request) {
        ArrayList<Booking> output = new ArrayList<>();

        OffsetDateTime start = request.getFilter().getStartedAtOrAfter();
        OffsetDateTime finish = request.getFilter().getEndedAtOrBefore();
        List<String> roomIds = request.getFilter().getRoomIdIn();
        List<String> emails = request.getFilter().getOwnerEmailIn();

        for (Booking booking : bookings) {
            if (
                    (roomIds.isEmpty() || roomIds.contains(booking.getRoom().getId()))
                    && (emails.isEmpty() || emails.contains(booking.getOwnerEmail()))
                    && (start == null || start.isBefore(booking.getStart()) || start.isEqual(booking.getStart()))
                    && (finish == null || finish.isAfter(booking.getEnd()) || finish.isEqual(booking.getEnd()))
            ) {
                output.add(booking);
            }
        }

        return output;
    }
}

