package at.srfg.iasset.repository.model.helper.value.type;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXsd;

import com.fasterxml.jackson.annotation.JsonValue;

import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;

public abstract class Value<T> {
	
	@JsonValue
	T value;
	
	public Value() {
		value = null; //
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
	
	public abstract Value<T> fromValue(String stringValue) throws ValueMappingException;
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
	/**
	 * Convert the provided string representation of an arbitrary value to it's 
	 * corresponding AAS typed {@link Value}
	 * @param xsd, The data type definition (see {@link DataTypeDefXsd}
	 * @param value The string value of the property 
	 * @return The typed {@link Value}
	 * @throws ValueMappingException 
	 */
	
	public static Value<?> getValue(DataTypeDefXsd xsd, String value) throws ValueMappingException {
		ValueType type = ValueType.fromDataType(xsd);
		try {
			@SuppressWarnings("rawtypes")
			Constructor<? extends Value> constructor = type.getValueClass().getConstructor();
			constructor.setAccessible(true);
			// construct new instance
			Value<?> theValue = constructor.newInstance();
			if (value!= null) {
				// provided string value might be null
				return theValue.fromValue(value);
			}
			return theValue;
		
		} catch (InvocationTargetException | InstantiationException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException e ){
			// TODO: Method must not return NULl
		}
		throw new ValueMappingException("Provided value cannot ");
	}
	public static  <T> T toValue(Type type, String value) throws ValueMappingException {
		ValueType valueType = ValueType.fromDataType(type);
		try {
			Constructor<? extends Value<?>> constructor = valueType.getValueClass().getConstructor();
			constructor.setAccessible(true);
			@SuppressWarnings("unchecked")
			Value<T> newVal = (Value<T>) constructor.newInstance();
			newVal.fromValue(value);
			return newVal.getValue();
		
		} catch (InvocationTargetException | InstantiationException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException e ){
			// TODO: report transformation errors
			e.printStackTrace();
			return null;
		}		
	}
	/**
	 * Transform the provided (typed) value into it's 
	 * string representation.
	 * @param <T> The type of the 
	 * @param value The value
	 * @return The string representation
	 * @throws ValueMappingException 
	 */
	public static <T> String fromValue(T value) throws ValueMappingException {
		if ( value != null) {
			
		}
		Type type = value.getClass();
		ValueType valueType = ValueType.fromDataType(type);
		try {
			Constructor<? extends Value<?>> constructor = valueType.getValueClass().getConstructor();
			constructor.setAccessible(true);
			@SuppressWarnings("unchecked")
			Value<T> val = (Value<T>) constructor.newInstance();
			val.setValue(value);
			return val.toString();
		} catch (InvocationTargetException | InstantiationException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException e ){
			// TODO: report transformation errors
			e.printStackTrace();
		
		}
		throw new ValueMappingException(String.format("Value Mapping failed for ", null) );
	}
}
