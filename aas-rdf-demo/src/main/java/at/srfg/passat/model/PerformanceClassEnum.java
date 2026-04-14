
package at.srfg.passat.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.processing.Generated;
import javax.xml.datatype.XMLGregorianCalendar;
import org.eclipse.esmf.aspectmodel.java.exception.EnumAttributeNotFoundException;

/**
 * Generated class PerformanceClassEnum
 * (urn:samm:at.srfg.passat.ski:1.0.0#PerformanceClassEnum).
 *
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Generated(value = "esmf-sdk 2.13.0", date = "2026-02-13T09:21:15.348+01")
public enum PerformanceClassEnum {
	R("R"), S("S"), C("C");

	private String value;

	PerformanceClassEnum(String value) {
		this.value = value;
	}

	@JsonCreator
	static PerformanceClassEnum enumDeserializationConstructor(String value) {
		return fromValue(value).orElseThrow(() -> new EnumAttributeNotFoundException("Tried to parse value \"" + value
				+ "\", but there is no enum field like that in PerformanceClassEnum"));
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	public static Optional<PerformanceClassEnum> fromValue(String value) {
		return Arrays.stream(PerformanceClassEnum.values()).filter(enumValue -> compareEnumValues(enumValue, value))
				.findAny();
	}

	private static boolean compareEnumValues(PerformanceClassEnum enumValue, String value) {
		return enumValue.getValue().equals(value);
	}

}
