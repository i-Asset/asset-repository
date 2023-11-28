package at.srfg.iasset.repository.model.operation;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import at.srfg.iasset.repository.model.InvocationRequest;

public abstract class AbstractInvocationRequest<T> implements InvocationRequest<T> {
	@JsonProperty("duration")
	private Duration duration;
	@JsonProperty("inputArguments")
	private List<T> inputArguments = new ArrayList<>();
	@JsonProperty("inoutputArguments")
	private List<T> inoutputArguments = new ArrayList<>();

	@Override
	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	@Override
	public Duration getDuration() {
		return duration; 
	}
	@Override
	public void setInputArguments(List<T> inputVariables) {
		this.inputArguments = inputVariables;
	}
	@Override
	public List<T> getInputArguments() {
		return inputArguments;
	}
	
	@Override
	public void setInoutputArguments(List<T> inoutputVariables) {
		this.inoutputArguments = inoutputVariables;
	}
	@Override
	public List<T> getInoutputArguments() {
		return inoutputArguments;
	}
	

}
