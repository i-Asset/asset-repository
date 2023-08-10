package at.srfg.iasset.repository.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets messageType
 */
public enum MessageType {
  UNDEFINED("Undefined"),
  
  INFO("Info"),
  
  WARNING("Warning"),
  
  ERROR("Error"),
  
  EXCEPTION("Exception");

  private String value;

  MessageType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static MessageType fromValue(String text) {
    for (MessageType b : MessageType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
