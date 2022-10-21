package at.srfg.iasset.repository.model.helper.value.type;

import com.fasterxml.jackson.annotation.JsonValue;

public abstract class Value<T> {
	
	@JsonValue
	T value;
	
	public Value() {
		//
	}
	public Value(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
	public abstract Value<T> fromValue(String stringValue);
	public abstract ValueType getValueType();
	
	public String toString() {
		return value != null ? value.toString() : "";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Value<?> other = (Value<?>) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}
