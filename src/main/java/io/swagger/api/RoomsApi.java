/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.46).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.BookRoomError;
import io.swagger.model.BookRoomRequest;
import io.swagger.model.Booking;
import io.swagger.model.GetFreeRoomsRequest;
import io.swagger.model.HTTPValidationError;
import io.swagger.model.Room;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-07-03T12:05:04.344081226Z[GMT]")
@Validated
public interface RoomsApi {

    @Operation(summary = "Book A Room", description = "", security = {
        @SecurityRequirement(name = "Bearer")    }, tags={ "Booking" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Room has been booked successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Booking.class))),
        
        @ApiResponse(responseCode = "400", description = "This room cannot be booked for this user during this time period", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookRoomError.class))),
        
        @ApiResponse(responseCode = "401", description = "API token was not provided, is invalid or has been expired"),
        
        @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationError.class))) })
    @RequestMapping(value = "/rooms/{room_id}/book",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<Booking> bookARoomRoomsRoomIdBookPost(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("room_id") String roomId, @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody BookRoomRequest body, @Parameter(in = ParameterIn.HEADER, description = "" ,schema=@Schema( defaultValue="en-US")) @RequestHeader(value="Accept-Language", required=false) String acceptLanguage);


    @Operation(summary = "Get All Bookable Rooms", description = "", security = {
        @SecurityRequirement(name = "Bearer")    }, tags={ "Booking" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Room.class)))),
        
        @ApiResponse(responseCode = "401", description = "API token was not provided, is invalid or has been expired"),
        
        @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationError.class))) })
    @RequestMapping(value = "/rooms",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<Room>> getAllBookableRoomsRoomsGet(@Parameter(in = ParameterIn.HEADER, description = "" ,schema=@Schema( defaultValue="en-US")) @RequestHeader(value="Accept-Language", required=false) String acceptLanguage);


    @Operation(summary = "Get Free Rooms", description = "Returns a list of rooms that are available for booking at the specified time period.", security = {
        @SecurityRequirement(name = "Bearer")    }, tags={ "Booking" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Room.class)))),
        
        @ApiResponse(responseCode = "401", description = "API token was not provided, is invalid or has been expired"),
        
        @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationError.class))) })
    @RequestMapping(value = "/rooms/free",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<List<Room>> getFreeRoomsRoomsFreePost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody GetFreeRoomsRequest body, @Parameter(in = ParameterIn.HEADER, description = "" ,schema=@Schema( defaultValue="en-US")) @RequestHeader(value="Accept-Language", required=false) String acceptLanguage);

}
