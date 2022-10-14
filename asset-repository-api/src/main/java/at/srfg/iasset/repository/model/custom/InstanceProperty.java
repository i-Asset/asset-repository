package at.srfg.iasset.repository.model.custom;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.impl.DefaultProperty;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class InstanceProperty extends DefaultProperty implements Property {
	public InstanceProperty() {
		
	}
	public InstanceProperty(Property other) {
		
		setCategory(other.getCategory());
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
		setValue(other.getValue());
		setValueId(other.getValueId());
		setValueType(other.getValueType());

	}
	@JsonIgnore
	private Consumer<String> consumer;
	@JsonIgnore
	private Supplier<String> supplier;
	public Consumer<String> consumer() {
		return consumer;
	}
	public void consumer(Consumer<String> consumer) {
		this.consumer = consumer;
	}
	public Supplier<String> supplier() {
		return supplier;
	}
	public void supplier(Supplier<String> supplier) {
		this.supplier = supplier;
	}
	@Override
	public String getValue() {
		if ( supplier != null) {
			return supplier.get();
		}
		return super.getValue();
	}
	public void setValue(String newValue) {
		if ( consumer != null) {
			consumer.accept(newValue);
		}
		else {
			super.setValue(newValue);
		}
	}

}
