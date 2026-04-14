
package at.srfg.passat.model.samm.generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.processing.Generated;

/**
 * Generated class for Identification Data
 * (urn:samm:at.srfg.passat.ski:1.0.0#IdentificationData). Entity containing
 * ski-identification data (item level)
 */
@Generated(value = "esmf-sdk 2.14.0", date = "2026-02-13T13:20:04.819+01")

public class IdentificationData {

	@NotNull
	private BigInteger serviceCount;

	@NotNull
	private String componentCode;

	@NotNull
	private String setCode;

	@NotNull
	private String serialNumber;

	@JsonCreator
	public IdentificationData(@JsonProperty(value = "serviceCount") BigInteger serviceCount,
			@JsonProperty(value = "componentCode") String componentCode,
			@JsonProperty(value = "setCode") String setCode,
			@JsonProperty(value = "serialNumber") String serialNumber) {
		super(

		);
		this.serviceCount = serviceCount;
		this.componentCode = componentCode;
		this.setCode = setCode;
		this.serialNumber = serialNumber;
	}

	/**
	 * Returns Service Count
	 *
	 * @return {@link #serviceCount}
	 */
	public BigInteger getServiceCount() {
		return this.serviceCount;
	}

	/**
	 * Returns Article Component Code
	 *
	 * @return {@link #componentCode}
	 */
	public String getComponentCode() {
		return this.componentCode;
	}

	/**
	 * Returns Ski Set Code
	 *
	 * @return {@link #setCode}
	 */
	public String getSetCode() {
		return this.setCode;
	}

	/**
	 * Returns Serial Number
	 *
	 * @return {@link #serialNumber}
	 */
	public String getSerialNumber() {
		return this.serialNumber;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final IdentificationData that = (IdentificationData) o;
		return Objects.equals(serviceCount, that.serviceCount) && Objects.equals(componentCode, that.componentCode)
				&& Objects.equals(setCode, that.setCode) && Objects.equals(serialNumber, that.serialNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serviceCount, componentCode, setCode, serialNumber);
	}
}
