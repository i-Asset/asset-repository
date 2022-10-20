package at.srfg.iasset.repository.model.value;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
	private Class<? extends Value> valueClass;
	
	
	
	private ValueType(Class<? extends Value> value, DataTypeDefXsd ... xsd) {
		this.valueClass = value;
		this.xsdTypes = Arrays.asList(xsd);
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
}
