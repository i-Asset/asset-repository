package at.srfg.iasset.repository.api.model;

import java.util.Objects;



/**
 * Message
 */


public class Message   {
  private String code = null;


  private MessageType messageType = null;

  private String text = null;

  private String timestamp = null;

  public Message code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * @return code
   **/
  
    public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Message messageType(MessageType messageType) {
    this.messageType = messageType;
    return this;
  }

  /**
   * Get messageType
   * @return messageType
   **/
  
    public MessageType getMessageType() {
    return messageType;
  }

  public void setMessageType(MessageType messageType) {
    this.messageType = messageType;
  }

  public Message text(String text) {
    this.text = text;
    return this;
  }

  /**
   * Get text
   * @return text
   **/
  
    public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Message timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   **/
  
    public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Message message = (Message) o;
    return Objects.equals(this.code, message.code) &&
        Objects.equals(this.messageType, message.messageType) &&
        Objects.equals(this.text, message.text) &&
        Objects.equals(this.timestamp, message.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, messageType, text, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Message {\n");
    
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    messageType: ").append(toIndentedString(messageType)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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
