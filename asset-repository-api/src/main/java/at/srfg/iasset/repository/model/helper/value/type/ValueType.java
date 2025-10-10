package at.srfg.iasset.repository.model.helper.value.type;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXsd;

import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;


public enum ValueType {
	STRING(		StringValue.class, 		DataTypeDefXsd.STRING, DataTypeDefXsd.ANY_URI),
	BOOLEAN(	BooleanValue.class, 	DataTypeDefXsd.BOOLEAN),
	DECIMAL(	DecimalValue.class, 	DataTypeDefXsd.DECIMAL),
	DOUBLE(		DoubleValue.class, 		DataTypeDefXsd.DOUBLE, DataTypeDefXsd.FLOAT),
	DURATION(	DurationValue.class, 	DataTypeDefXsd.DURATION),
	INTEGER(	IntegerValue.class, 	DataTypeDefXsd.INTEGER, DataTypeDefXsd.INT),
	LONG(		LongValue.class, 		DataTypeDefXsd.LONG),
	BINARY(		BinaryValue.class, 		DataTypeDefXsd.BASE64BINARY),
	DATE_TIME(	DateTimeValue.class, 	DataTypeDefXsd.DATE_TIME),
	SHORT(		ShortValue.class,		DataTypeDefXsd.SHORT),
	DATE(		DateValue.class, 		DataTypeDefXsd.DATE),
	TIME(		TimeValue.class, 		DataTypeDefXsd.TIME),
	;
	
	private List<DataTypeDefXsd> xsdTypes;
	private Class<? extends Value<?>> valueClass;
	private Type reflectionType;
	
	Class<? extends Value<?>> getValueClass() {
		return valueClass;
	}
	
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
//	/**
//	 * Obtain the typed Value representation from a given {@link SubmodelElement}.
//	 * <p>For successful operationb, the provided {@link SubmodelElement} must 
//	 * provide the methods
//	 * <ul>
//	 * <li>String getValue()
//	 * <li>DataTypeXsd getValueType()
//	 * </ul>
//	 * 
//	 * </p>
//	 * @param <T>
//	 * @param clazz
//	 * @return
//	 */
//	public static <T extends DataElement> Value<?> getValue(T clazz) {
//		try {
//			Method valueMethod = clazz.getClass().getMethod("getValue");
//			Object value = valueMethod.invoke(clazz);
//			Method valueTypeMethod = clazz.getClass().getMethod("getValueType");
//			Object valueType = valueTypeMethod.invoke(clazz);
//			
//			return getValue((DataTypeDefXsd) valueType, value.toString());
//		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}

	/**
	 * Identify the {@link ValueType} based on given {@link DataTypeDefXsd} enumeration
	 * @param xsd The AAS {@link DataTypeDefXsd}
	 * @return The {@link ValueType}
	 */
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
	public static  <T> T toValue(Type type, String value) throws ValueMappingException {
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
			throw new ValueMappingException(e);
		}		
	}
}
