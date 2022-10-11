package at.srfg.iasset.repository.model.custom;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.impl.DefaultProperty;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class InstanceProperty extends DefaultProperty implements Property {
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
