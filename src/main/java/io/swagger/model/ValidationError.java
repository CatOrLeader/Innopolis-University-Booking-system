package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ValidationError
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-07-03T12:05:04.344081226Z[GMT]")


public class ValidationError   {
  @JsonProperty("loc")
  @Valid
  private List<AnyOfValidationErrorLocItems> loc = new ArrayList<AnyOfValidationErrorLocItems>();

  @JsonProperty("msg")
  private String msg = null;

  @JsonProperty("type")
  private String type = null;

  public ValidationError loc(List<AnyOfValidationErrorLocItems> loc) {
    this.loc = loc;
    return this;
  }

  public ValidationError addLocItem(AnyOfValidationErrorLocItems locItem) {
    this.loc.add(locItem);
    return this;
  }

  /**
   * Get loc
   * @return loc
   **/
  @Schema(required = true, description = "")
      @NotNull

    public List<AnyOfValidationErrorLocItems> getLoc() {
    return loc;
  }

  public void setLoc(List<AnyOfValidationErrorLocItems> loc) {
    this.loc = loc;
  }

  public ValidationError msg(String msg) {
    this.msg = msg;
    return this;
  }

  /**
   * Get msg
   * @return msg
   **/
  @Schema(required = true, description = "")
      @NotNull

    public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public ValidationError type(String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   **/
  @Schema(required = true, description = "")
      @NotNull

    public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidationError validationError = (ValidationError) o;
    return Objects.equals(this.loc, validationError.loc) &&
        Objects.equals(this.msg, validationError.msg) &&
        Objects.equals(this.type, validationError.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(loc, msg, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidationError {\n");
    
    sb.append("    loc: ").append(toIndentedString(loc)).append("\n");
    sb.append("    msg: ").append(toIndentedString(msg)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
