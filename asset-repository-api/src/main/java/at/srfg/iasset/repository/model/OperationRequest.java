package at.srfg.iasset.repository.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.eclipse.aas4j.v3.model.OperationVariable;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * OperationRequest
 */
@Validated
public class OperationRequest {
	@JsonProperty("inoutputArguments")
	@Valid
	private List<OperationVariable> inoutputArguments = null;

	@JsonProperty("inputArguments")
	@Valid
	private List<OperationVariable> inputArguments = null;

	@JsonProperty("requestId")
	private String requestId = null;

	@JsonProperty("timeout")
	private Integer timeout = null;

	public OperationRequest inoutputArguments(List<OperationVariable> inoutputArguments) {
		this.inoutputArguments = inoutputArguments;
		return this;
	}

	public OperationRequest addInoutputArgumentsItem(OperationVariable inoutputArgumentsItem) {
		if (this.inoutputArguments == null) {
			this.inoutputArguments = new ArrayList<OperationVariable>();
		}
		this.inoutputArguments.add(inoutputArgumentsItem);
		return this;
	}

	/**
	 * Get inoutputArguments
	 * 
	 * @return inoutputArguments
	 **/
	@Schema(description = "")
	@Valid
	public List<OperationVariable> getInoutputArguments() {
		return inoutputArguments;
	}

	public void setInoutputArguments(List<OperationVariable> inoutputArguments) {
		this.inoutputArguments = inoutputArguments;
	}

	public OperationRequest inputArguments(List<OperationVariable> inputArguments) {
		this.inputArguments = inputArguments;
		return this;
	}

	public OperationRequest addInputArgumentsItem(OperationVariable inputArgumentsItem) {
		if (this.inputArguments == null) {
			this.inputArguments = new ArrayList<OperationVariable>();
		}
		this.inputArguments.add(inputArgumentsItem);
		return this;
	}

	/**
	 * Get inputArguments
	 * 
	 * @return inputArguments
	 **/
	@Schema(description = "")
	@Valid
	public List<OperationVariable> getInputArguments() {
		return inputArguments;
	}

	public void setInputArguments(List<OperationVariable> inputArguments) {
		this.inputArguments = inputArguments;
	}

	public OperationRequest requestId(String requestId) {
		this.requestId = requestId;
		return this;
	}

	/**
	 * Get requestId
	 * 
	 * @return requestId
	 **/
	@Schema(description = "")

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public OperationRequest timeout(Integer timeout) {
		this.timeout = timeout;
		return this;
	}

	/**
	 * Get timeout
	 * 
	 * @return timeout
	 **/
	@Schema(description = "")

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		OperationRequest operationRequest = (OperationRequest) o;
		return Objects.equals(this.inoutputArguments, operationRequest.inoutputArguments)
				&& Objects.equals(this.inputArguments, operationRequest.inputArguments)
				&& Objects.equals(this.requestId, operationRequest.requestId)
				&& Objects.equals(this.timeout, operationRequest.timeout);
	}

	@Override
	public int hashCode() {
		return Objects.hash(inoutputArguments, inputArguments, requestId, timeout);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class OperationRequest {\n");

		sb.append("    inoutputArguments: ").append(toIndentedString(inoutputArguments)).append("\n");
		sb.append("    inputArguments: ").append(toIndentedString(inputArguments)).append("\n");
		sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
		sb.append("    timeout: ").append(toIndentedString(timeout)).append("\n");
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
