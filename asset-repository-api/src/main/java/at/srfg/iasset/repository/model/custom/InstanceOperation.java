package at.srfg.iasset.repository.model.custom;

import java.util.Map;
import java.util.function.Function;

import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.impl.DefaultOperation;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class InstanceOperation extends DefaultOperation implements Operation {
	@JsonIgnore
	private Function<Map<String,Object>, Object> function;

	public Function<Map<String, Object>, Object> getFunction() {
		return function;
	}

	public void setFunction(Function<Map<String, Object>, Object> function) {
		this.function = function;
	}

}
