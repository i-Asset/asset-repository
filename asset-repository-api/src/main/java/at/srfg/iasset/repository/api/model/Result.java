package at.srfg.iasset.repository.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Result
 */
public class Result {
	private List<Message> messages = null;

	private Boolean success = null;

	public Result messages(List<Message> messages) {
		this.messages = messages;
		return this;
	}

	public Result addMessagesItem(Message messagesItem) {
		if (this.messages == null) {
			this.messages = new ArrayList<Message>();
		}
		this.messages.add(messagesItem);
		return this;
	}

	/**
	 * Get messages
	 * 
	 * @return messages
	 **/
	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Result success(Boolean success) {
		this.success = success;
		return this;
	}

	/**
	 * Get success
	 * 
	 * @return success
	 **/

	public Boolean isSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Result result = (Result) o;
		return Objects.equals(this.messages, result.messages) && Objects.equals(this.success, result.success);
	}

	@Override
	public int hashCode() {
		return Objects.hash(messages, success);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Result {\n");

		sb.append("    messages: ").append(toIndentedString(messages)).append("\n");
		sb.append("    success: ").append(toIndentedString(success)).append("\n");
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
