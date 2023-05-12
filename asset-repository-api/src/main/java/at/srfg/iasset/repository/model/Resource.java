package at.srfg.iasset.repository.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Resource
 */
@Validated
@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-08-06T18:15:09.134Z[GMT]")


public class Resource   {
  @JsonProperty("path")
  private String path = null;

  @JsonProperty("contentType")
  private String contentType = null;

  public Resource path(String path) {
    this.path = path;
    return this;
  }

  /**
   * Get path
   * @return path
   **/
  @Schema(required = true, description = "")
      @NotNull

  @Pattern(regexp="^([!#$%&'*+\\-.^_`|~0-9a-zA-Z])+/([!#$%&'*+\\-.^_`|~0-9a-zA-Z])+([  ]*;[  ]*([!#$%&'*+\\-.^_`|~0-9a-zA-Z])+=(([!#$%&'*+\\-.^_`|~0-9a-zA-Z])+|\"(([  !#-\\[\\]-~]|[\\x80-\\xff])|\\\\([  !-~]|[\\x80-\\xff]))*\"))*$")   public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Resource contentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  /**
   * Get contentType
   * @return contentType
   **/
  @Schema(description = "")
  
  @Pattern(regexp="^([!#$%&'*+\\-.^_`|~0-9a-zA-Z])+/([!#$%&'*+\\-.^_`|~0-9a-zA-Z])+([  ]*;[  ]*([!#$%&'*+\\-.^_`|~0-9a-zA-Z])+=(([!#$%&'*+\\-.^_`|~0-9a-zA-Z])+|\"(([  !#-\\[\\]-~]|[\\x80-\\xff])|\\\\([  !-~]|[\\x80-\\xff]))*\"))*$") @Size(min=1)   public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Resource resource = (Resource) o;
    return Objects.equals(this.path, resource.path) &&
        Objects.equals(this.contentType, resource.contentType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(path, contentType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Resource {\n");
    
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    contentType: ").append(toIndentedString(contentType)).append("\n");
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
