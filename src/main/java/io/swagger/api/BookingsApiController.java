package io.swagger.api;

import io.swagger.database.Database;
import io.swagger.model.Booking;
import io.swagger.model.HTTPValidationError;
import io.swagger.model.QueryBookingsRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-07-03T12:05:04.344081226Z[GMT]")
@RestController
public class BookingsApiController implements BookingsApi {

    private static final Logger log = LoggerFactory.getLogger(BookingsApiController.class);
    private final Database database = Database.getInstance();
    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public BookingsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Object> deleteABookingBookingsBookingIdDelete(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("booking_id") String bookingId, @Parameter(in = ParameterIn.HEADER, description = "", schema = @Schema(defaultValue = "en-US")) @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        Boolean flag = database.deleteBooking(bookingId);
        return new ResponseEntity<Object>(
                (flag == true ?
                        "Booking was deleted successfully" : "Booking with such ID is not found"),
                (flag == true ? HttpStatus.OK : HttpStatus.NOT_FOUND));
    }

    public ResponseEntity queryBookingsBookingsQueryPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody QueryBookingsRequest body, @Parameter(in = ParameterIn.HEADER, description = "", schema = @Schema(defaultValue = "en-US")) @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            return new ResponseEntity<List<Booking>>(database.takeNeededBookings(body), HttpStatus.OK);
        }

        return new ResponseEntity<List<Booking>>(HttpStatus.NOT_IMPLEMENTED);
    }

}
