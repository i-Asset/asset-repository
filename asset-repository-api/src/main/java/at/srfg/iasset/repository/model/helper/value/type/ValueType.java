package at.srfg.iasset.repository.model.helper.value.type;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.aas4j.v3.model.DataTypeDefXsd;
import org.eclipse.aas4j.v3.model.SubmodelElement;


public enum ValueType {
	STRING(		StringValue.class, 		DataTypeDefXsd.STRING, DataTypeDefXsd.ANY_URI),
	BOOLEAN(	BooleanValue.class, 	DataTypeDefXsd.BOOLEAN),
	DECIMAL(	DecimalValue.class, 	DataTypeDefXsd.DECIMAL),
	DOUBLE(		DoubleValue.class, 		DataTypeDefXsd.DOUBLE),
	INTEGER(	IntegerValue.class, 	DataTypeDefXsd.INTEGER, DataTypeDefXsd.INT),
	BINARY(		BinaryValue.class, 		DataTypeDefXsd.BASE64BINARY),
	DATE_TIME(	DateTimeValue.class, 	DataTypeDefXsd.DATE_TIME),
	SHORT(		ShortValue.class,		DataTypeDefXsd.SHORT),
	;
	
	private List<DataTypeDefXsd> xsdTypes;
	private Class<? extends Value<?>> valueClass;
	private Type reflectionType;
	
	
	
	private ValueType(Class<? extends Value<?>> value, DataTypeDefXsd ... xsd) {
		this.valueClass = value;
		this.xsdTypes = Arrays.asList(xsd);
		this.reflectionType = extractType(valueClass);
	}
	private Type extractType(Class<? extends Value<?>> valueClass) {
		// we 
		ParameterizedType superClass = (ParameterizedType) valueClass.getGenericSuperclass();
//		ParameterizedType aType = (ParameterizedType) superClass.getGenericInterfaces()[0];
		return superClass.getActualTypeArguments()[0];
	}
	public static <T extends SubmodelElement> Value<?> getValue(T clazz) {
		try {
			Method valueMethod = clazz.getClass().getMethod("getValue");
			Object value = valueMethod.invoke(clazz);
			Method valueTypeMethod = clazz.getClass().getMethod("getValueType");
			Object valueType = valueTypeMethod.invoke(clazz);
			
			return getValue((DataTypeDefXsd) valueType, value.toString());
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Value<?> getValue(DataTypeDefXsd xsd, String value) {
		ValueType type = fromDataType(xsd);
		try {
			@SuppressWarnings("rawtypes")
			Constructor<? extends Value> constructor = type.valueClass.getConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance().fromValue(value);
		
		} catch (InvocationTargetException | InstantiationException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException e ){
			
		}
		return null;
	}
	public static ValueType fromDataType(DataTypeDefXsd xsd) {
		return Stream.of(values())
				.filter(x -> x.xsdTypes.contains(xsd))
				.findAny()
				.orElse(STRING);
		
	}
	public static ValueType fromDataType(Type type) {
		return Stream.of(values())
				.filter(x -> x.reflectionType.equals(type))
				.findAny()
				.orElse(STRING);
		
	}
	public static  <T> String fromValue(T value) {
		Type type = value.getClass();
		ValueType valueType = fromDataType(type);
		try {
			Constructor<? extends Value<?>> constructor = valueType.valueClass.getConstructor();
			constructor.setAccessible(true);
			@SuppressWarnings("unchecked")
			Value<T> val = (Value<T>) constructor.newInstance();
			val.setValue(value);
			return val.toString();
		
		} catch (InvocationTargetException | InstantiationException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException e ){
			// TODO: report transformation errors
			return null;
		}		
	}
	public static  <T> T toValue(Type type, String value) {
		ValueType valueType = fromDataType(type);
		try {
			Constructor<? extends Value<?>> constructor = valueType.valueClass.getConstructor();
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
}
