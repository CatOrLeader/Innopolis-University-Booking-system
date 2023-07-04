package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * BookingsFilter
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-07-03T12:05:04.344081226Z[GMT]")


public class BookingsFilter   {
  @JsonProperty("started_at_or_after")
  private OffsetDateTime startedAtOrAfter = null;

  @JsonProperty("ended_at_or_before")
  private OffsetDateTime endedAtOrBefore = null;

  @JsonProperty("room_id_in")
  @Valid
  private List<String> roomIdIn = null;

  @JsonProperty("owner_email_in")
  @Valid
  private List<String> ownerEmailIn = null;

  public BookingsFilter startedAtOrAfter(OffsetDateTime startedAtOrAfter) {
    this.startedAtOrAfter = startedAtOrAfter;
    return this;
  }

  /**
   * When specified, only bookings that started at this time or later will be returned.
   * @return startedAtOrAfter
   **/
  @Schema(description = "When specified, only bookings that started at this time or later will be returned.")
  
    @Valid
    public OffsetDateTime getStartedAtOrAfter() {
    return startedAtOrAfter;
  }

  public void setStartedAtOrAfter(OffsetDateTime startedAtOrAfter) {
    this.startedAtOrAfter = startedAtOrAfter;
  }

  public BookingsFilter endedAtOrBefore(OffsetDateTime endedAtOrBefore) {
    this.endedAtOrBefore = endedAtOrBefore;
    return this;
  }

  /**
   * When specified, only bookings that ended at this time or sooner will be returned.
   * @return endedAtOrBefore
   **/
  @Schema(description = "When specified, only bookings that ended at this time or sooner will be returned.")
  
    @Valid
    public OffsetDateTime getEndedAtOrBefore() {
    return endedAtOrBefore;
  }

  public void setEndedAtOrBefore(OffsetDateTime endedAtOrBefore) {
    this.endedAtOrBefore = endedAtOrBefore;
  }

  public BookingsFilter roomIdIn(List<String> roomIdIn) {
    this.roomIdIn = roomIdIn;
    return this;
  }

  public BookingsFilter addRoomIdInItem(String roomIdInItem) {
    if (this.roomIdIn == null) {
      this.roomIdIn = new ArrayList<String>();
    }
    this.roomIdIn.add(roomIdInItem);
    return this;
  }

  /**
   * When specified, only bookings of the rooms from the list will be returned.
   * @return roomIdIn
   **/
  @Schema(description = "When specified, only bookings of the rooms from the list will be returned.")
  
    public List<String> getRoomIdIn() {
    return roomIdIn;
  }

  public void setRoomIdIn(List<String> roomIdIn) {
    this.roomIdIn = roomIdIn;
  }

  public BookingsFilter ownerEmailIn(List<String> ownerEmailIn) {
    this.ownerEmailIn = ownerEmailIn;
    return this;
  }

  public BookingsFilter addOwnerEmailInItem(String ownerEmailInItem) {
    if (this.ownerEmailIn == null) {
      this.ownerEmailIn = new ArrayList<String>();
    }
    this.ownerEmailIn.add(ownerEmailInItem);
    return this;
  }

  /**
   * When specified, only bookings with the owner with email address from the list will be returned.
   * @return ownerEmailIn
   **/
  @Schema(description = "When specified, only bookings with the owner with email address from the list will be returned.")
  
    public List<String> getOwnerEmailIn() {
    return ownerEmailIn;
  }

  public void setOwnerEmailIn(List<String> ownerEmailIn) {
    this.ownerEmailIn = ownerEmailIn;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BookingsFilter bookingsFilter = (BookingsFilter) o;
    return Objects.equals(this.startedAtOrAfter, bookingsFilter.startedAtOrAfter) &&
        Objects.equals(this.endedAtOrBefore, bookingsFilter.endedAtOrBefore) &&
        Objects.equals(this.roomIdIn, bookingsFilter.roomIdIn) &&
        Objects.equals(this.ownerEmailIn, bookingsFilter.ownerEmailIn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startedAtOrAfter, endedAtOrBefore, roomIdIn, ownerEmailIn);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BookingsFilter {\n");
    
    sb.append("    startedAtOrAfter: ").append(toIndentedString(startedAtOrAfter)).append("\n");
    sb.append("    endedAtOrBefore: ").append(toIndentedString(endedAtOrBefore)).append("\n");
    sb.append("    roomIdIn: ").append(toIndentedString(roomIdIn)).append("\n");
    sb.append("    ownerEmailIn: ").append(toIndentedString(ownerEmailIn)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
