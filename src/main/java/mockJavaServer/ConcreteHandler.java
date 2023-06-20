package mockJavaServer;

import APIWrapper.json.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

class ConcreteHandler implements HttpHandler{
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String handle(HttpRequest request, HttpResponse response, Database database) throws IOException {
        PossibleRequests flag = defineRequest(request);

        if (flag == null) {
            return gson.toJson(new HTTPValidationError(
                    new ValidationError(
                            "Concrete Handler, in handle() method",
                            "something went wrotg with your http request," +
                                    "connect to the server provider (Artur)",
                            "Wrong http request"
                    )
            ));
        }

        switch (Objects.requireNonNull(flag)) {
            case GET_ROOMS -> {
                return processGetRooms(database);
            }
            case POST_FREE_ROOMS -> {
                return processGetFreeRooms(database);
            }
            case BOOK -> {
                return processBookRoom(request, database);
            }
            case POST_BOOKINGS -> {
                return processGetBookings(request, database);
            }
            case DELETE_BOOKING -> {
                return processDeleteBooking(request, response, database);
            }
            case DEFAULT -> {
                DatabaseJSON databaseJSON = new DatabaseJSON(
                        database.rooms, database.freeRooms, database.bookings);
                return gson.toJson(databaseJSON);
            }
        }
        return null;
    }

    private PossibleRequests defineRequest(HttpRequest request) {
        if (request.getUrl().equals("/rooms")) {
            return PossibleRequests.GET_ROOMS;
        } else if (request.getUrl().equals("/rooms/free")) {
            return PossibleRequests.POST_FREE_ROOMS;
        } else if (request.getUrl().split("/").length == 4) {
            return PossibleRequests.BOOK;
        } else if (request.getUrl().equals("/bookings/query")) {
            return PossibleRequests.POST_BOOKINGS;
        } else if (request.getUrl().matches("/bookings/.*")) {
            return PossibleRequests.DELETE_BOOKING;
        } else if (request.getUrl().equals("/")) {
            return PossibleRequests.DEFAULT;
        } else {
            return null;
        }
    }

    private String processGetRooms(Database database) {
        return gson.toJson(database.rooms);
    }

    private String processGetFreeRooms(Database database) {
        return gson.toJson(database.freeRooms);
    }
    private String processBookRoom(HttpRequest request, Database database) throws IOException {
        String roomId = request.getUrl().split("/")[2];
        BookRoomRequest bookRoomRequest = gson.fromJson(request.getBody(), BookRoomRequest.class);

        Booking booking = new Booking(
                String.valueOf(database.bookings.size() + 1),
                bookRoomRequest.title,
                bookRoomRequest.start,
                bookRoomRequest.end,
                database.getRoom(roomId),
                bookRoomRequest.owner_email
        );

        if (booking.room == null) {
            return gson.toJson(new BookRoomError("This place was already booked"));
        }

        database.makeBusy(roomId);

        database.addBooking(booking);

        updateJson(gson.toJson(new DatabaseJSON(database.rooms, database.freeRooms, database.bookings)));

        return gson.toJson(booking, Booking.class);
    }
    private String processGetBookings(HttpRequest request, Database database) {
        QueryBookingsRequest queryBookingsRequest =
                gson.fromJson(request.getBody(), QueryBookingsRequest.class);

        ArrayList<Booking> neededBookings = database.takeNeededBookings(
                null,
                new ArrayList<>(Arrays.asList(queryBookingsRequest.filter.room_id_in)),
                new ArrayList<>(Arrays.asList(queryBookingsRequest.filter.owner_email_in))
        );

        return gson.toJson(neededBookings);
    }
    private String processDeleteBooking(HttpRequest request, HttpResponse response, Database database)
            throws IOException {
        String bookingId = request.getUrl().split("/")[2];

        Booking booking = database.getBookings(bookingId);
        boolean flag = database.deleteBooking(bookingId);

        if (!flag) {
            return gson.toJson("Delete was unsuccessful. Wrong booking ID");
        }

        database.makeFree(booking.room.id);

        updateJson(gson.toJson(new DatabaseJSON(database.rooms, database.freeRooms, database.bookings)));

        return gson.toJson("Delete was successfully made. Deleted booking number: " + bookingId);
    }

    private void updateJson(String body) throws IOException {
        BufferedWriter writer = new BufferedWriter(
                new FileWriter("src/main/java/mockTestingForDevs/database.json"));
        writer.write(body);
        writer.close();
    }
}

enum PossibleRequests {
    GET_ROOMS, POST_FREE_ROOMS, BOOK, POST_BOOKINGS, DELETE_BOOKING, DEFAULT
}
