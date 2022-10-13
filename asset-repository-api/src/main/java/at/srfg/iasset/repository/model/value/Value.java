package at.srfg.iasset.repository.model.value;

public abstract class Value<T> {
	
	
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
	
}
