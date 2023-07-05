package io.swagger.api;

import io.swagger.database.BookRoomException;
import io.swagger.database.Database;
import io.swagger.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-07-03T12:05:04.344081226Z[GMT]")
@RestController
public class RoomsApiController implements RoomsApi {
    private static final Logger log = LoggerFactory.getLogger(RoomsApiController.class);

    private final Database database = Database.getInstance();

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public RoomsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity bookARoomRoomsRoomIdBookPost(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("room_id") String roomId, @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody BookRoomRequest body, @Parameter(in = ParameterIn.HEADER, description = "" ,schema=@Schema( defaultValue="en-US")) @RequestHeader(value="Accept-Language", required=false) String acceptLanguage) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Booking>(database.createBooking(roomId, body), HttpStatus.OK);
            } catch (BookRoomException e) {
                return new ResponseEntity<BookRoomError>(new BookRoomError().message(
                        "This room cannot be booked for this user during this time period"
                ), HttpStatus.valueOf(400));
            }
        }

        return new ResponseEntity<Booking>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity getAllBookableRoomsRoomsGet(@Parameter(in = ParameterIn.HEADER, description = "" ,schema=@Schema( defaultValue="en-US")) @RequestHeader(value="Accept-Language", required=false) String acceptLanguage) {
        return new ResponseEntity<List<Room>>(database.rooms, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity getFreeRoomsRoomsFreePost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody GetFreeRoomsRequest body,@Parameter(in = ParameterIn.HEADER, description = "" ,schema=@Schema( defaultValue="en-US")) @RequestHeader(value="Accept-Language", required=false) String acceptLanguage) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            return new ResponseEntity<List<Room>>(database.getFreeRoomsInTime(body), HttpStatus.OK);
        }

        return new ResponseEntity<List<Room>>(HttpStatus.NOT_IMPLEMENTED);
    }

}
