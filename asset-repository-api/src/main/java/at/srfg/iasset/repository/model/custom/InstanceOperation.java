package at.srfg.iasset.repository.model.custom;

import java.util.Map;
import java.util.function.Function;

import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.impl.DefaultOperation;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class InstanceOperation extends DefaultOperation implements Operation {
	public InstanceOperation() {
		
	}
	public InstanceOperation(Operation other) {
		setChecksum(other.getChecksum());
		setDataSpecifications(other.getDataSpecifications());
		setDescriptions(other.getDescriptions());
		setDisplayNames(other.getDisplayNames());
		setEmbeddedDataSpecifications(other.getEmbeddedDataSpecifications());
		setExtensions(other.getExtensions());
		setIdShort(other.getIdShort());
		setKind(other.getKind());
		setQualifiers(other.getQualifiers());
		setSemanticId(other.getSemanticId());
		setSupplementalSemanticIds(other.getSupplementalSemanticIds());
		setInoutputVariables(other.getInoutputVariables());
		setInputVariables(other.getInputVariables());
		setOutputVariables(other.getOutputVariables());
	}
	
	@JsonIgnore
	private Function<Object, Object> function;

	public Function<Object, Object> function() {
		return function;
	}
	public Object invoke(Object parameter) {
		if ( function != null) {
			return function.apply(parameter);
		}
		return null;
	}
	public void function(Function<Object, Object> function) {
		this.function = function;
	}

}
