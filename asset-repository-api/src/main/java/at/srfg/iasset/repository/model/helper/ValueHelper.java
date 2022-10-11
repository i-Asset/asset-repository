package at.srfg.iasset.repository.model.helper;

import java.time.Duration;
import java.time.LocalDate;

import org.eclipse.aas4j.v3.model.DataTypeDefXsd;

public class ValueHelper {
	
	public static Object getValue(DataTypeDefXsd type, String value) {
		switch(type) {
		case ANY_URI:
		case STRING:
			return value;
		case INT:
		case NON_NEGATIVE_INTEGER:
		case NON_POSITIVE_INTEGER:
		case UNSIGNED_INT: 
		case UNSIGNED_LONG:
		case UNSIGNED_SHORT:
		case INTEGER:
		case SHORT:
			return Integer.valueOf(value);
		case DECIMAL:
		case FLOAT:
		case DOUBLE:
			return Double.valueOf(value);
		case BOOLEAN:
			return Boolean.valueOf(value);
		case BASE64BINARY: 
			return value.getBytes();
		case DURATION:
			return Duration.parse(value);
		case DATE:
			return LocalDate.parse(value);
		default:
			return value;
		}
	}

}
